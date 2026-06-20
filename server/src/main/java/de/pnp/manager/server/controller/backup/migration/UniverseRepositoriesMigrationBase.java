package de.pnp.manager.server.controller.backup.migration;

import de.pnp.manager.server.controller.backup.IBackupMigration;
import org.bson.Document;

import static de.pnp.manager.server.controller.backup.BackupExportController.REPOSITORY_CONTENT;
import static de.pnp.manager.server.controller.backup.BackupExportController.REPOSITORY_NAME;

/**
 * Base class to migrate repositories in a universe.
 */
public abstract class UniverseRepositoriesMigrationBase implements IBackupMigration {

    private final String repositoryName;

    protected UniverseRepositoriesMigrationBase(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    @Override
    public void migrateRepository(Document repositoryDocument) {
        if (!repositoryName.equals(repositoryDocument.getString(REPOSITORY_NAME))) {
            return;
        }
        for (Document document : repositoryDocument.getList(REPOSITORY_CONTENT, Document.class)) {
            migrate(document);
        }
    }

    /**
     * Migrate a document in-place for the repository.
     */
    protected abstract void migrate(Document document);
}
