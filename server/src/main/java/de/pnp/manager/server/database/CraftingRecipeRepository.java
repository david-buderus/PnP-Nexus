package de.pnp.manager.server.database;

import de.pnp.manager.component.crafting.CraftingRecipe;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link CraftingRecipe crafting recipes}
 */
@Component
public class CraftingRecipeRepository extends RepositoryBase<CraftingRecipe> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "fabrication";

    public CraftingRecipeRepository() {
        super(CraftingRecipe.class, REPOSITORY_NAME);
    }
}
