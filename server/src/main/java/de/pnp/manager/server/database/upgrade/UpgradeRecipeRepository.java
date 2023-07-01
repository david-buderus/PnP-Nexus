package de.pnp.manager.server.database.upgrade;

import de.pnp.manager.component.upgrade.UpgradeRecipe;
import de.pnp.manager.server.database.RepositoryBase;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link UpgradeRecipe upgrade recipes}.
 */
@Component
public class UpgradeRecipeRepository extends RepositoryBase<UpgradeRecipe> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "upgrade-recipe";
    
    public UpgradeRecipeRepository() {
        super(UpgradeRecipe.class, REPOSITORY_NAME);
    }
}
