package de.pnp.manager.server.controller.backup;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import de.pnp.manager.component.Universe;
import de.pnp.manager.server.database.MongoConfig;
import de.pnp.manager.server.database.UniverseRepository;
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
        codecRegistry = mongoClient.getDatabase(UniverseRepository.DATABASE_NAME).getCodecRegistry();
    }

    /**
     * Exports all universes as a zip into the given {@link OutputStream}.
     */
    public void exportUniverses(OutputStream outputStream) throws IOException {
        List<String> universeNames = universeRepository.getAll().stream().map(Universe::getName)
            .toList();
        if (universeNames.isEmpty()) {
            return;
        }
        exportUniverses(outputStream, universeNames);
    }

    /**
     * Exports the given universes as a zip into the given {@link OutputStream}.
     */
    public void exportUniverses(OutputStream outputStream, Collection<String> universes)
        throws IOException {
        EncoderContext context = EncoderContext.builder().isEncodingCollectibleDocument(true).build();

        try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
            for (String universe : universes) {
                if (!universeRepository.exists(universe)) {
                    throw new IllegalArgumentException("Universe " + universe + " does not exist.");
                }
                exportUniverse(zipOut, universe, mongoClient.getDatabase(universe), context);
            }

            Document metadata = new Document();
            metadata.put(VERSION, EBackupVersion.CURRENT);

            ZipEntry zipEntry = new ZipEntry(METADATA_FILE);
            zipOut.putNextEntry(zipEntry);
            zipOut.write(encode(metadata, codecRegistry, context));
            zipOut.closeEntry();
        }
    }

    private void exportUniverse(ZipOutputStream outputStream, String universe,
        MongoDatabase universeDatabase, EncoderContext context)
        throws IOException {

        ZipEntry universeEntry = new ZipEntry(universe + "/" + UNIVERSE_FILE);
        outputStream.putNextEntry(universeEntry);
        Document universeDocument = mongoConfig.mongoTemplate(UniverseRepository.DATABASE_NAME)
            .findById(universe, Document.class, UniverseRepository.REPOSITORY_NAME);
        outputStream.write(encode(universeDocument, codecRegistry, context));
        outputStream.closeEntry();

        for (String repositoryName : universeDatabase.listCollectionNames()) {

            List<Document> repositoryContent = mongoConfig.mongoTemplate(universe)
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
