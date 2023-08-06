package de.pnp.manager.server.service;

import de.pnp.manager.component.IRecipeEntry.CharacterResourceRecipeEntry;
import de.pnp.manager.component.IRecipeEntry.ECharacterResource;
import de.pnp.manager.component.IRecipeEntry.ItemRecipeEntry;
import de.pnp.manager.component.IRecipeEntry.MaterialRecipeEntry;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.upgrade.UpgradeRecipe;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.upgrade.UpgradeRecipeRepository;
import de.pnp.manager.utils.TestItemBuilder;
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
        super(upgradeRecipeService);
    }

    @Override
    protected List<UpgradeRecipe> createObjects() {
        Material material = materialRepository.insert(universeName, new Material(null, "Mat", List.of()));
        return List.of(new UpgradeRecipe(null, createUpgrade().withName("A").buildPersisted(), List.of(), "",
                List.of(new ItemRecipeEntry(7, createItem().buildPersisted(TestItemBuilder::buildItem)))),
            new UpgradeRecipe(null, createUpgrade().withName("B").buildPersisted(),
                List.of(createUpgrade().withName("B2").buildPersisted()), "",
                List.of(new MaterialRecipeEntry(2, material))),
            new UpgradeRecipe(null, createUpgrade().withName("C").buildPersisted(), List.of(), "",
                List.of(new CharacterResourceRecipeEntry(100, ECharacterResource.MENTAL_HEALTH))));
    }
}