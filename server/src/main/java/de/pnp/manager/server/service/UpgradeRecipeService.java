package de.pnp.manager.server.service;

import de.pnp.manager.component.upgrade.UpgradeRecipe;
import de.pnp.manager.server.database.upgrade.UpgradeRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link UpgradeRecipeRepository}.
 */
@RestController
@RequestMapping("api/{universe}/upgrade-recipes")
public class UpgradeRecipeService extends RepositoryServiceBase<UpgradeRecipe, UpgradeRecipeRepository> {

    public UpgradeRecipeService(@Autowired UpgradeRecipeRepository repository) {
        super(repository);
    }
}
