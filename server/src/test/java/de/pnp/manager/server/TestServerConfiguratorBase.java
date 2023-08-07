package de.pnp.manager.server;

import de.pnp.manager.server.controller.backup.BackupImportController;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public abstract class TestServerConfiguratorBase {
    @Autowired
    private BackupImportController backupImportController;
    private final @Nullable Path backupZip;

    public TestServerConfiguratorBase(@Nullable Path backupZip) {
        this.backupZip = backupZip;
    }

    public abstract void configure();

    public void loadBackup() {
        if (backupZip == null) {
            return;
        }

        try (FileInputStream inputStream = new FileInputStream(backupZip.toFile())) {
            backupImportController.importBackup(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}