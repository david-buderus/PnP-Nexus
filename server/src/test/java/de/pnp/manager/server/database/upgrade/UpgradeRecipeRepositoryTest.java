package de.pnp.manager.server.database.upgrade;

import de.pnp.manager.component.IRecipeEntry.CharacterResource;
import de.pnp.manager.component.IRecipeEntry.CharacterResourceRecipeEntry;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.UpgradeRecipe;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import de.pnp.manager.server.database.RepositoryTestBase;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link UpgradeRecipeRepository}.
 */
class UpgradeRecipeRepositoryTest extends RepositoryTestBase<UpgradeRecipe, UpgradeRecipeRepository> {

    @Autowired
    private ItemTypeRepository typeRepository;

    @Autowired
    private UpgradeRepository upgradeRepository;

    public UpgradeRecipeRepositoryTest(@Autowired UpgradeRecipeRepository repository) {
        super(repository);
    }

    @Test
    void testUpgradeLink() {
        ItemType type = typeRepository.insert(universeName, new ItemType(null, "Type", ETypeRestriction.ITEM));
        Upgrade upgradeA = upgradeRepository.insert(universeName, new Upgrade(null, "Shine A", type, 1, 10,
            List.of(SimpleUpgradeEffect.create("The weapon emits light"))));
        Upgrade upgradeB = new Upgrade(null, "Shine B", type, 1, 10,
            List.of(SimpleUpgradeEffect.create("The weapon emits a lot of light")));
        UpgradeRecipe recipe = new UpgradeRecipe(null, upgradeA, List.of(), "", List.of());

        testRepositoryLink(UpgradeRecipe::getUpgrade, upgradeRepository, recipe, upgradeA, upgradeB);
    }

    @Test
    void testNecessaryUpgradeLink() {
        ItemType type = typeRepository.insert(universeName, new ItemType(null, "Type", ETypeRestriction.ITEM));
        Upgrade result = upgradeRepository.insert(universeName, new Upgrade(null, "Result", type, 1, 10, List.of()));
        Upgrade upgradeA = upgradeRepository.insert(universeName, new Upgrade(null, "Shine A", type, 1, 10,
            List.of(SimpleUpgradeEffect.create("The weapon emits light"))));
        Upgrade upgradeB = new Upgrade(null, "Shine B", type, 1, 10,
            List.of(SimpleUpgradeEffect.create("The weapon emits a lot of light")));
        UpgradeRecipe recipe = new UpgradeRecipe(null, result, List.of(upgradeA), "", List.of());

        testRepositoryCollectionLink(UpgradeRecipe::getRequiredUpgrades, upgradeRepository, recipe, List.of(upgradeA),
            Map.of(upgradeA, upgradeB));
    }

    @Override
    protected UpgradeRecipe createObject() {
        ItemType type = typeRepository.insert(universeName, new ItemType(null, "Test-Type", ETypeRestriction.ITEM));
        Upgrade upgrade = upgradeRepository.insert(universeName, new Upgrade(null, "Test", type, 1, 0, List.of()));
        return new UpgradeRecipe(null, upgrade, List.of(), "", List.of());
    }

    @Override
    protected UpgradeRecipe createSlightlyChangeObject() {
        ItemType type = typeRepository.insert(universeName,
            new ItemType(null, "Other Test-Type", ETypeRestriction.ITEM));
        Upgrade upgrade = upgradeRepository.insert(universeName,
            new Upgrade(null, "Other Test", type, 1, 0, List.of()));
        return new UpgradeRecipe(null, upgrade, List.of(), "Something",
            List.of(new CharacterResourceRecipeEntry(10, CharacterResource.MANA)));
    }

    @Override
    protected List<UpgradeRecipe> createMultipleObjects() {
        ItemType type = typeRepository.insert(universeName, new ItemType(null, "Test-Type", ETypeRestriction.ITEM));
        Upgrade upgrade1 = upgradeRepository.insert(universeName, new Upgrade(null, "Test 1", type, 1, 0, List.of()));
        Upgrade upgrade2 = upgradeRepository.insert(universeName, new Upgrade(null, "Test 2", type, 1, 0, List.of()));
        return List.of(new UpgradeRecipe(null, upgrade1, List.of(), "", List.of()),
            new UpgradeRecipe(null, upgrade2, List.of(upgrade1), "", List.of()));
    }
}