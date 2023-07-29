package de.pnp.manager.server.database.upgrade;

import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.server.database.RepositoryBase;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link Upgrade upgrades}.
 */
@Component
public class UpgradeRepository extends RepositoryBase<Upgrade> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "upgrade";

    public UpgradeRepository() {
        super(Upgrade.class, REPOSITORY_NAME);
    }
}
