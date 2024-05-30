package de.pnp.manager.server.service.upgrade;

import de.pnp.manager.component.IResourceUsage.CharacterResourceUsage;
import de.pnp.manager.component.IResourceUsage.ItemUsage;
import de.pnp.manager.component.IResourceUsage.MaterialUsage;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.upgrade.UpgradeRecipe;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
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

    @Autowired
    private SecondaryAttributeRepository attributeRepository;

    public UpgradeRecipeServiceTest(@Autowired UpgradeRecipeService upgradeRecipeService,
        @Autowired UpgradeRecipeRepository repository) {
        super(upgradeRecipeService, repository, UpgradeRecipe.class);
    }

    @Override
    protected List<UpgradeRecipe> createObjects() {
        SecondaryAttribute mentalHealth = createSecondaryAttribute().withName("Mental Health").isConsumable().persist()
            .build();
        Material material = materialRepository.insert(getUniverseName(), new Material(null, "Mat", List.of()));
        return List.of(new UpgradeRecipe(null, createUpgrade().withName("A").persist().build(), List.of(), "",
                List.of(new ItemUsage(7, createItem().persist().buildItem()))),
            new UpgradeRecipe(null, createUpgrade().withName("B").persist().build(),
                List.of(createUpgrade().withName("B2").persist().build()), "",
                List.of(new MaterialUsage(2, material))),
            new UpgradeRecipe(null, createUpgrade().withName("C").persist().build(), List.of(), "",
                List.of(new CharacterResourceUsage(100, mentalHealth))));
    }
}