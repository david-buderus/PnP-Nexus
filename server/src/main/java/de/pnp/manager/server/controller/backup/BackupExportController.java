package de.pnp.manager.server.controller.backup;

import static de.pnp.manager.server.database.DatabaseConstants.METADATA_DATABASE;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.database.DatabaseConstants;
import de.pnp.manager.server.database.MongoConfig;
import de.pnp.manager.server.database.UniverseRepository;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.UserRepository;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.bson.BsonBinaryWriter;
import org.bson.Document;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.io.BasicOutputBuffer;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A controller to create backups of a complete PnP-Nexus instance.
 */
@Component
public class BackupExportController {

    /**
     * The key to access the content of a repository document.
     * <p>
     * The value behind this key is a list of all documents of a repository.
     */
    public static final String REPOSITORY_CONTENT = "content";

    /**
     * The key to access the name of a repository document.
     * <p>
     * The value behind this key is the identifier of a repository.
     */
    public static final String REPOSITORY_NAME = "name";

    /**
     * The key to access the version of a backup.
     *
     * @see #METADATA_FILE
     */
    public static final String VERSION = "version";

    /**
     * The file name of a universe in a folder in the backup.
     */
    public static final String UNIVERSE_FILE = "universe";

    /**
     * The file name of the file which contains all metadata of a backup.
     */
    public static final String METADATA_FILE = "metadata";

    /**
     * The file name of the file which contains all user data of a backup.
     */
    public static final String USER_FILE = "users";

    /**
     * The key to access the users of a backup.
     *
     * @see #USER_FILE
     */
    public static final String USER_REPOSITORY = "user";

    /**
     * The key to access the user details of a backup.
     *
     * @see #USER_FILE
     */
    public static final String USER_DETAILS_REPOSITORY = "details";

    private final MongoClient mongoClient;
    private final MongoConfig mongoConfig;
    private final UniverseRepository universeRepository;
    private final CodecRegistry codecRegistry;

    public BackupExportController(@Autowired MongoClient mongoClient,
        @Autowired MongoConfig mongoConfig,
        @Autowired UniverseRepository universeRepository) {
        this.mongoClient = mongoClient;
        this.mongoConfig = mongoConfig;
        this.universeRepository = universeRepository;
        codecRegistry = mongoClient.getDatabase(DatabaseConstants.METADATA_DATABASE).getCodecRegistry();
    }

    /**
     * Exports the given universes and global data as a zip into the given {@link OutputStream}.
     * <p>
     * If the given universe list is empty or null, all universes will be exported.
     */
    public void export(OutputStream outputStream, @Nullable Collection<String> universeNames) throws IOException {
        if (universeNames == null || universeNames.isEmpty()) {
            universeNames = universeRepository.getAll().stream().map(Universe::getName)
                .toList();
        }

        EncoderContext context = EncoderContext.builder().isEncodingCollectibleDocument(true).build();

        try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
            exportMetaData(context, zipOut);
            exportUniverses(context, zipOut, universeNames);
        }
    }

    private void exportMetaData(EncoderContext context, ZipOutputStream zipOut) throws IOException {
        Document metadata = new Document();
        metadata.put(VERSION, EBackupVersion.CURRENT);

        ZipEntry metadataEntry = new ZipEntry(METADATA_FILE);
        zipOut.putNextEntry(metadataEntry);
        zipOut.write(encode(metadata, codecRegistry, context));
        zipOut.closeEntry();

        Document userData = new Document();

        List<Document> userRepositoryContent = mongoConfig.mongoTemplate(METADATA_DATABASE)
            .findAll(Document.class, UserRepository.REPOSITORY_NAME);
        userData.put(USER_REPOSITORY, userRepositoryContent);
        List<Document> userDetailsRepositoryContent = mongoConfig.mongoTemplate(METADATA_DATABASE)
            .findAll(Document.class, UserDetailsRepository.REPOSITORY_NAME);
        userData.put(USER_DETAILS_REPOSITORY, userDetailsRepositoryContent);

        ZipEntry userEntry = new ZipEntry(USER_FILE);
        zipOut.putNextEntry(userEntry);
        zipOut.write(encode(userData, codecRegistry, context));
        zipOut.closeEntry();
    }

    /**
     * Exports the given universes as a zip into the given {@link OutputStream}.
     */
    private void exportUniverses(EncoderContext context, ZipOutputStream zipOut, Collection<String> universes)
        throws IOException {
        for (String universe : universes) {
            if (!universeRepository.exists(universe)) {
                throw new IllegalArgumentException("Universe " + universe + " does not exist.");
            }
            exportUniverse(zipOut, universe, mongoClient.getDatabase(universe), context);
        }
    }

    private void exportUniverse(ZipOutputStream outputStream, String universe,
        MongoDatabase universeDatabase, EncoderContext context)
        throws IOException {

        ZipEntry universeEntry = new ZipEntry(universe + "/" + UNIVERSE_FILE);
        outputStream.putNextEntry(universeEntry);
        Document universeDocument = mongoConfig.mongoTemplate(DatabaseConstants.METADATA_DATABASE)
            .findById(universe, Document.class, UniverseRepository.REPOSITORY_NAME);
        outputStream.write(encode(universeDocument, codecRegistry, context));
        outputStream.closeEntry();

        for (String repositoryName : universeDatabase.listCollectionNames()) {

            List<Document> repositoryContent = mongoConfig.universeMongoTemplate(universe)
                .findAll(Document.class, repositoryName);
            Document repository = new Document();
            repository.put(REPOSITORY_CONTENT, repositoryContent);
            repository.put(REPOSITORY_NAME, repositoryName);

            ZipEntry zipEntry = new ZipEntry(universe + "/" + repositoryName);
            outputStream.putNextEntry(zipEntry);
            outputStream.write(encode(repository, codecRegistry, context));
            outputStream.closeEntry();
        }
    }

    private static byte[] encode(Document document, CodecRegistry registry, EncoderContext context) {
        try (BasicOutputBuffer buffer = new BasicOutputBuffer()) {
            BsonBinaryWriter writer = new BsonBinaryWriter(buffer);
            registry.get(Document.class).encode(writer, document, context);
            return buffer.toByteArray();
        }
    }
}
