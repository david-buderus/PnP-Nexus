package de.pnp.manager.server.service;

import de.pnp.manager.component.CraftingRecipe;
import de.pnp.manager.server.database.CraftingRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link CraftingRecipeRepository}.
 */
@RestController
@RequestMapping("/api/{universe}/crafting-recipes")
public class CraftingRecipeService extends RepositoryServiceBase<CraftingRecipe, CraftingRecipeRepository> {

    protected CraftingRecipeService(@Autowired CraftingRecipeRepository repository) {
        super(repository);
    }
}
