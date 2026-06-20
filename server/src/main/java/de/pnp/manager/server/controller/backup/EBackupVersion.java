package de.pnp.manager.server.controller.backup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The versions of the backup.
 */
public enum EBackupVersion {
    VERSION_1(List.of());

    /**
     * The current version of the backup, created by the current version.
     */
    public static final EBackupVersion CURRENT = values()[values().length - 1];

    private final List<IBackupMigration> migrations;

    EBackupVersion(List<IBackupMigration> migrations) {
        this.migrations = migrations;
    }

    /**
     * Returns a list of all necessary {@link IBackupMigration migrations} to reach the
     * {@link #CURRENT current version}.
     */
    public List<? extends IBackupMigration> getNecessaryMigrations() {
        if (this == CURRENT) {
            return Collections.emptyList();
        }

        EBackupVersion[] values = values();
        return Arrays.asList(values).subList(this.ordinal() + 1, values.length)
                .stream().flatMap(v -> v.migrations.stream()).toList();
    }
}
