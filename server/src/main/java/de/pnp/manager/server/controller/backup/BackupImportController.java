package de.pnp.manager.server.controller.backup;

import static de.pnp.manager.server.controller.backup.BackupExportController.REPOSITORY_CONTENT;
import static de.pnp.manager.server.controller.backup.BackupExportController.REPOSITORY_NAME;
import static de.pnp.manager.server.controller.backup.BackupExportController.UNIVERSE_FILE;
import static org.springframework.data.mongodb.core.mapping.BasicMongoPersistentProperty.ID_FIELD_NAME;

import com.mongodb.client.MongoClient;
import de.pnp.manager.server.database.MongoConfig;
import de.pnp.manager.server.database.UniverseRepository;
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
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

/**
 * A controller to import backups of complete universes.
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
  public void importUniverses(InputStream inputStream) throws IOException {
    File tmpDir = null;
    try {
      tmpDir = Files.createTempDirectory("pnp-nexus-backup").toFile();

      writeZipToDir(inputStream, tmpDir);
      importUniverses(tmpDir);
    } finally {
      if (tmpDir != null) {
        FileSystemUtils.deleteRecursively(tmpDir);
      }
    }
  }

  private void importUniverses(File tmpDir) throws IOException {
    DecoderContext decoderContext = DecoderContext.builder().build();

    Document metadata = decode(new File(tmpDir, BackupExportController.METADATA_FILE),
        codecRegistry, decoderContext);

    EBackupVersion backupVersion = EBackupVersion.valueOf(
        metadata.get(BackupExportController.VERSION,
            String.class));
    List<? extends IBackupMigration> migrations = backupVersion.getNecessaryMigrations();

    for (File universeFolder : Objects.requireNonNull(tmpDir.listFiles(File::isDirectory))) {
      Document universeDocument = decode(new File(universeFolder, UNIVERSE_FILE),
          codecRegistry, decoderContext);
      migrations.forEach(migration -> migration.migrateUniverse(universeDocument));
      String universeName = universeDocument.getString(ID_FIELD_NAME);

      if (universeRepository.exists(universeName)) {
        throw new IllegalArgumentException("Universe " + universeName + " does already exists.");
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
   * Writes the zip in the given {@link InputStream} into the given {@link File directory}.
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
      int len;
      while ((len = zis.read(buffer)) > 0) {
        fos.write(buffer, 0, len);
      }
    }
  }
}
