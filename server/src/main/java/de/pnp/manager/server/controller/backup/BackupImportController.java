package de.pnp.manager.server.controller.backup;

import static de.pnp.manager.server.controller.backup.BackupExportController.REPOSITORY_CONTENT;
import static de.pnp.manager.server.controller.backup.BackupExportController.REPOSITORY_NAME;
import static de.pnp.manager.server.controller.backup.BackupExportController.UNIVERSE_FILE;
import static de.pnp.manager.server.controller.backup.BackupExportController.USER_DETAILS_REPOSITORY;
import static de.pnp.manager.server.controller.backup.BackupExportController.USER_FILE;
import static de.pnp.manager.server.controller.backup.BackupExportController.USER_REPOSITORY;
import static org.springframework.data.mongodb.core.mapping.BasicMongoPersistentProperty.ID_FIELD_NAME;

import com.mongodb.client.MongoClient;
import de.pnp.manager.server.database.MongoConfig;
import de.pnp.manager.server.database.UniverseRepository;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.UserRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.bson.BsonBinaryReader;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

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
        codecRegistry = mongoClient.getDatabase(UniverseRepository.DATABASE_NAME).getCodecRegistry();
        this.universeRepository = universeRepository;
    }

    /**
     * Imports the backup in the given {@link InputStream}.
     */
    public void importBackup(InputStream inputStream) throws IOException {
        File tmpDir = null;
        try {
            tmpDir = Files.createTempDirectory("pnp-nexus-backup").toFile();

            writeZipToDir(inputStream, tmpDir);
            importBackup(tmpDir);
        } finally {
            if (tmpDir != null) {
                FileSystemUtils.deleteRecursively(tmpDir);
            }
        }
    }

    private void importBackup(File tmpDir) throws IOException {
        DecoderContext decoderContext = DecoderContext.builder().build();

        Document metadata = decode(new File(tmpDir, BackupExportController.METADATA_FILE),
            codecRegistry, decoderContext);

        EBackupVersion backupVersion = EBackupVersion.valueOf(
            metadata.get(BackupExportController.VERSION,
                String.class));
        List<? extends IBackupMigration> migrations = backupVersion.getNecessaryMigrations();

        importGlobalData(tmpDir, decoderContext, migrations);

        importUniverses(tmpDir, decoderContext, migrations);
    }

    private void importGlobalData(File tmpDir, DecoderContext decoderContext,
        List<? extends IBackupMigration> migrations) throws IOException {

        Document userDocument = decode(new File(tmpDir, USER_FILE),
            codecRegistry, decoderContext);
        List<Document> userRepository = userDocument.getList(USER_REPOSITORY, Document.class);
        userRepository.forEach(user -> migrations.forEach(migration -> migration.migrateUsers(user)));
        List<Document> userDetailsRepository = userDocument.getList(USER_DETAILS_REPOSITORY, Document.class);
        userDetailsRepository.forEach(
            userDetails -> migrations.forEach(migration -> migration.migrateUserDetails(userDetails)));

        MongoTemplate mongoTemplate = mongoConfig.mongoTemplate(UniverseRepository.DATABASE_NAME);

        List<Object> ids = userRepository.stream().map(doc -> doc.get("_id")).toList();
        mongoTemplate.findAllAndRemove(Query.query(Criteria.where("_id").in(ids)), UserRepository.REPOSITORY_NAME);
        mongoTemplate.findAllAndRemove(Query.query(Criteria.where("_id").in(ids)),
            UserDetailsRepository.REPOSITORY_NAME);

        mongoTemplate.insert(userRepository, UserRepository.REPOSITORY_NAME);
        mongoTemplate.insert(userDetailsRepository, UserDetailsRepository.REPOSITORY_NAME);
    }

    private void importUniverses(File tmpDir, DecoderContext decoderContext,
        List<? extends IBackupMigration> migrations)
        throws IOException {
        for (File universeFolder : Objects.requireNonNull(tmpDir.listFiles(File::isDirectory))) {
            Document universeDocument = decode(new File(universeFolder, UNIVERSE_FILE),
                codecRegistry, decoderContext);
            migrations.forEach(migration -> migration.migrateUniverse(universeDocument));
            String universeName = universeDocument.getString(ID_FIELD_NAME);

            if (universeRepository.exists(universeName)) {
                throw new IllegalArgumentException("Universe " + universeName + " does already exist.");
            }

            mongoConfig.mongoTemplate(UniverseRepository.DATABASE_NAME)
                .insert(universeDocument, UniverseRepository.REPOSITORY_NAME);

            MongoTemplate mongoTemplate = mongoConfig.mongoTemplate(universeName);

            for (File repositoryFile : Objects.requireNonNull(
                universeFolder.listFiles(file -> !UNIVERSE_FILE.equals(file.getName())))) {

                Document repositoryDocument = decode(repositoryFile, codecRegistry, decoderContext);
                migrations.forEach(migration -> migration.migrateRepository(repositoryDocument));

                mongoTemplate.insert(repositoryDocument.getList(REPOSITORY_CONTENT, Document.class),
                    repositoryDocument.getString(REPOSITORY_NAME));
            }
        }
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
}
