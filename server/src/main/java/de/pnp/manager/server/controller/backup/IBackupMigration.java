package de.pnp.manager.server.controller.backup;

import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.database.RepositoryBase;
import java.util.List;
import org.bson.Document;

/**
 * A definition of a migration of a {@link EBackupVersion}.
 */
public interface IBackupMigration {

    /**
     * The input is a document that describes a {@link Universe}.
     */
    default void migrateUniverse(Document universeDocument) {
        // no op
    }

    /**
     * The input is a document that describes a {@link RepositoryBase}.
     */
    default void migrateRepository(Document repositoryDocument) {
        // no op
    }

    /**
     * The input is a document that describes a repository which contains metadata.
     */
    default void migrateMetadata(String repositoryName, List<Document> repositoryContent) {
        // no op
    }
}
