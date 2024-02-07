package de.pnp.manager.server.controller.backup;

import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.server.database.RepositoryBase;
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
     * The input is a document that describes a {@link PnPUser}.
     */
    default void migrateUsers(Document user) {

    }

    /**
     * The input is a document that describes a {@link PnPUserDetails}.
     */
    default void migrateUserDetails(Document userDetails) {

    }
}
