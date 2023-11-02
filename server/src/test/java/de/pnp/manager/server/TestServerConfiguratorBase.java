package de.pnp.manager.server;

import static org.junit.jupiter.api.Assertions.fail;

import de.pnp.manager.server.controller.backup.BackupImportController;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Configures a test server on startup.
 */
public abstract class TestServerConfiguratorBase {

    @Autowired
    private BackupImportController backupImportController;

    @Nullable
    private final Path backupZip;

    public TestServerConfiguratorBase(@Nullable Path backupZip) {
        this.backupZip = backupZip;
    }

    /**
     * Configures the test server.
     * <p>
     * Will be called after the backup has been loaded.
     */
    public abstract void configure();

    /**
     * Loads the backup from the disk if the {@link #backupZip path} is not null.
     */
    public void loadBackup() {
        if (backupZip == null) {
            return;
        }

        try (FileInputStream inputStream = new FileInputStream(backupZip.toFile())) {
            backupImportController.importBackup(inputStream);
        } catch (IOException e) {
            fail(e);
        }
    }
}