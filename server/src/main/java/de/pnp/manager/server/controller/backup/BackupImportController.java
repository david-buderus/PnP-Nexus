package de.pnp.manager.server.controller.backup;

import com.mongodb.client.MongoClient;
import de.pnp.manager.server.database.DatabaseConstants;
import de.pnp.manager.server.database.MongoConfig;
import de.pnp.manager.server.database.universe.UniverseRepository;
import org.bson.BsonBinaryReader;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static de.pnp.manager.server.controller.backup.BackupExportController.*;
import static org.springframework.data.mongodb.core.mapping.BasicMongoPersistentProperty.ID_FIELD_NAME;

/**
 * A controller to import backups of a whole PnP-Nexus instance.
 */
@Component
public class BackupImportController {

    private final MongoConfig mongoConfig;
    private final CodecRegistry codecRegistry;
    private final UniverseRepository universeRepository;

    public BackupImportController(@Autowired MongoClient mongoClient,
                                  @Autowired MongoConfig mongoConfig, @Autowired UniverseRepository universeRepository) {
        this.mongoConfig = mongoConfig;
        codecRegistry = mongoClient.getDatabase(DatabaseConstants.METADATA_DATABASE).getCodecRegistry();
        this.universeRepository = universeRepository;
    }

    /**
     * Imports the backup in the given {@link InputStream}.
     */
    public void importBackup(InputStream inputStream) throws IOException {
        importBackup(inputStream, false);
    }
    
    /**
     * Imports the backup in the given {@link InputStream}.
     * <p>
     * Should only be used by tests.
     *
     * @return The remapping of the ObjectIds if it was used.
     */
    public Map<ObjectId, ObjectId> importBackup(InputStream inputStream, boolean remapUniverseIds) throws IOException {
        File tmpDir = null;
        try {
            tmpDir = Files.createTempDirectory("pnp-nexus-backup").toFile();

            writeZipToDir(inputStream, tmpDir);
            return importBackup(tmpDir, remapUniverseIds);
        } finally {
            if (tmpDir != null) {
                FileSystemUtils.deleteRecursively(tmpDir);
            }
        }
    }

    private Map<ObjectId, ObjectId> importBackup(File tmpDir, boolean remapUniverseIds) throws IOException {
        DecoderContext decoderContext = DecoderContext.builder().build();

        Document metadata = decode(new File(tmpDir, METADATA_FILE),
                codecRegistry, decoderContext);

        EBackupVersion backupVersion = EBackupVersion.valueOf(
                metadata.get(BackupExportController.VERSION,
                        String.class));
        List<? extends IBackupMigration> migrations = backupVersion.getNecessaryMigrations();

        importMetaData(tmpDir, decoderContext, migrations);

        return importUniverses(tmpDir, decoderContext, migrations, remapUniverseIds);
    }

    private void importMetaData(File tmpDir, DecoderContext decoderContext,
                                List<? extends IBackupMigration> migrations) throws IOException {

        MongoTemplate mongoTemplate = mongoConfig.mongoTemplate(DatabaseConstants.METADATA_DATABASE);

        for (File repositoryFile : Objects.requireNonNullElse(
                tmpDir.listFiles(BackupImportController::isRepositoryFile), new File[0])) {
            Document repositoryDocument = decode(repositoryFile, codecRegistry, decoderContext);
            String repositoryName = repositoryDocument.getString(REPOSITORY_NAME);
            List<Document> repositoryContent = repositoryDocument.getList(REPOSITORY_CONTENT, Document.class);

            migrations.forEach(migration -> migration.migrateMetadata(repositoryName, repositoryContent));

            List<Object> ids = repositoryContent.stream().map(doc -> doc.get("_id")).toList();
            mongoTemplate.findAllAndRemove(Query.query(Criteria.where("_id").in(ids)), repositoryName);

            mongoTemplate.insert(repositoryContent, repositoryName);
        }
    }

    private Map<ObjectId, ObjectId> importUniverses(File tmpDir, DecoderContext decoderContext,
                                                    List<? extends IBackupMigration> migrations, boolean remapUniverseIds)
            throws IOException {
        Map<ObjectId, ObjectId> result = new HashMap<>();

        for (File universeFolder : Objects.requireNonNull(tmpDir.listFiles(File::isDirectory))) {
            Document universeDocument = decode(new File(universeFolder, UNIVERSE_FILE),
                    codecRegistry, decoderContext);
            if (remapUniverseIds) {
                ObjectId newId = new ObjectId();
                result.put(universeDocument.getObjectId(ID_FIELD_NAME), newId);
                universeDocument.put(ID_FIELD_NAME, newId);
            }
            migrations.forEach(migration -> migration.migrateUniverse(universeDocument));
            ObjectId universeName = universeDocument.getObjectId(ID_FIELD_NAME);

            if (universeRepository.exists(universeName)) {
                throw new IllegalArgumentException("Universe " + universeName + " does already exist.");
            }

            mongoConfig.mongoTemplate(DatabaseConstants.METADATA_DATABASE)
                    .insert(universeDocument, UniverseRepository.REPOSITORY_NAME);

            MongoTemplate mongoTemplate = mongoConfig.universeMongoTemplate(universeName);

            for (File repositoryFile : Objects.requireNonNull(
                    universeFolder.listFiles(file -> !UNIVERSE_FILE.equals(file.getName())))) {

                Document repositoryDocument = decode(repositoryFile, codecRegistry, decoderContext);
                migrations.forEach(migration -> migration.migrateRepository(repositoryDocument));

                mongoTemplate.insert(repositoryDocument.getList(REPOSITORY_CONTENT, Document.class),
                        repositoryDocument.getString(REPOSITORY_NAME));
            }
        }

        return result;
    }

    private static Document decode(File file, CodecRegistry registry, DecoderContext context)
            throws IOException {
        try (FileInputStream universeFileStream = new FileInputStream(file);
             BsonBinaryReader reader = new BsonBinaryReader(
                     ByteBuffer.wrap(universeFileStream.readAllBytes()))) {
            return registry.get(Document.class).decode(reader, context);
        }
    }

    /**
     * Unzips the archive given by the {@link InputStream} into the given {@link File directory}.
     */
    private static void writeZipToDir(InputStream inputStream, File destDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(inputStream)) {
            ZipEntry zipEntry = zis.getNextEntry();

            byte[] buffer = new byte[1024];
            while (zipEntry != null) {
                File newFile = new File(destDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    mkdirs(newFile);
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    mkdirs(parent);

                    writeFile(zis, buffer, newFile);
                }
                zipEntry = zis.getNextEntry();
            }
        }
    }

    private static void mkdirs(File parent) throws IOException {
        if (!parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + parent);
        }
    }

    private static void writeFile(ZipInputStream zis, byte[] buffer, File newFile)
            throws IOException {
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            int len = zis.read(buffer);
            while (len > 0) {
                fos.write(buffer, 0, len);
                len = zis.read(buffer);
            }
        }
    }

    private static boolean isRepositoryFile(File file) {
        return file.isFile() && !file.getName().equals(METADATA_FILE);
    }
}
