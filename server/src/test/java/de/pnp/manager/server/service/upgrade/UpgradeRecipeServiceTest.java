package de.pnp.manager.server.service.upgrade;

import de.pnp.manager.component.IRecipeEntry.CharacterResourceRecipeEntry;
import de.pnp.manager.component.IRecipeEntry.ECharacterResource;
import de.pnp.manager.component.IRecipeEntry.ItemRecipeEntry;
import de.pnp.manager.component.IRecipeEntry.MaterialRecipeEntry;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.upgrade.UpgradeRecipe;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.upgrade.UpgradeRecipeRepository;
import de.pnp.manager.server.service.RepositoryServiceBaseTest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link UpgradeRecipeService}
 */
class UpgradeRecipeServiceTest extends
    RepositoryServiceBaseTest<UpgradeRecipe, UpgradeRecipeRepository, UpgradeRecipeService> {

    @Autowired
    private MaterialRepository materialRepository;

    public UpgradeRecipeServiceTest(@Autowired UpgradeRecipeService upgradeRecipeService) {
        super(upgradeRecipeService, UpgradeRecipe.class);
    }

    @Override
    protected List<UpgradeRecipe> createObjects() {
        Material material = materialRepository.insert(getUniverseName(), new Material(null, "Mat", List.of()));
        return List.of(new UpgradeRecipe(null, createUpgrade().withName("A").persist().build(), List.of(), "",
                List.of(new ItemRecipeEntry(7, createItem().persist().buildItem()))),
            new UpgradeRecipe(null, createUpgrade().withName("B").persist().build(),
                List.of(createUpgrade().withName("B2").persist().build()), "",
                List.of(new MaterialRecipeEntry(2, material))),
            new UpgradeRecipe(null, createUpgrade().withName("C").persist().build(), List.of(), "",
                List.of(new CharacterResourceRecipeEntry(100, ECharacterResource.MENTAL_HEALTH))));
    }
}