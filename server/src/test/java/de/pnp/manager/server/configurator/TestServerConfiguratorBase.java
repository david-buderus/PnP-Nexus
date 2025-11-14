package de.pnp.manager.server.configurator;

import de.pnp.manager.server.controller.backup.BackupImportController;
import org.bson.types.ObjectId;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Configures a test server on startup.
 */
public abstract class TestServerConfiguratorBase {

    @Autowired
    private BackupImportController backupImportController;

    @Nullable
    private final File backupZip;

    protected TestServerConfiguratorBase(@Nullable File backupZip) {
        this.backupZip = backupZip;
    }

    /**
     * Configures the test server.
     * <p>
     * Will be called after the backup has been loaded.
     */
    public abstract Map<ObjectId, ObjectId> configure();

    /**
     * Loads the backup from the disk if the {@link #backupZip path} is not null.
     *
     * @return The remapping of the ObjectIds imported by the backup.
     */
    public Map<ObjectId, ObjectId> loadBackup() {
        if (backupZip == null) {
            return Map.of();
        }

        try (FileInputStream inputStream = new FileInputStream(backupZip)) {
            return backupImportController.importBackup(inputStream, true);
        } catch (IOException e) {
            return fail(e);
        }
    }
}