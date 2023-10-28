package de.pnp.manager.server.database.upgrade;

import de.pnp.manager.component.IRecipeEntry.CharacterResourceRecipeEntry;
import de.pnp.manager.component.IRecipeEntry.ECharacterResource;
import de.pnp.manager.component.IRecipeEntry.ItemRecipeEntry;
import de.pnp.manager.component.item.Item;
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
        Upgrade upgradeA = createUpgrade().withName("Shine A")
            .addEffect(SimpleUpgradeEffect.create("The weapon emits light")).persist().build();
        Upgrade upgradeB = createUpgrade().withName("Shine B")
            .addEffect(SimpleUpgradeEffect.create("The weapon emits light")).build();
        UpgradeRecipe recipe = new UpgradeRecipe(null, upgradeA, List.of(), "",
            List.of(new CharacterResourceRecipeEntry(1, ECharacterResource.MANA)));

        testRepositoryLink(UpgradeRecipe::getUpgrade, upgradeRepository, recipe, upgradeA, upgradeB);
    }

    @Test
    void testNecessaryUpgradeLink() {
        Upgrade result = createUpgrade().withName("Result").persist().build();
        Upgrade upgradeA = createUpgrade().withName("Shine A")
            .addEffect(SimpleUpgradeEffect.create("The weapon emits light")).persist().build();

        Upgrade upgradeB = createUpgrade().withName("Shine B")
            .addEffect(SimpleUpgradeEffect.create("The weapon emits a lot of light")).build();
        UpgradeRecipe recipe = new UpgradeRecipe(null, result, List.of(upgradeA), "",
            List.of(new CharacterResourceRecipeEntry(100, ECharacterResource.MANA)));

        testRepositoryCollectionLink(UpgradeRecipe::getRequiredUpgrades, upgradeRepository, recipe, List.of(upgradeA),
            Map.of(upgradeA, upgradeB));
    }

    @Override
    protected UpgradeRecipe createObject() {
        ItemType type = typeRepository.insert(getUniverseName(),
            new ItemType(null, "Test-Type", ETypeRestriction.ITEM));
        Upgrade upgrade = createUpgrade().withName("Test").withTarget(type).persist().build();
        return new UpgradeRecipe(null, upgrade, List.of(), "",
            List.of(new CharacterResourceRecipeEntry(10, ECharacterResource.HEALTH)));
    }

    @Override
    protected UpgradeRecipe createSlightlyChangeObject() {
        ItemType type = typeRepository.insert(getUniverseName(),
            new ItemType(null, "Other Test-Type", ETypeRestriction.ITEM));
        Upgrade upgrade = createUpgrade().withName("Other Test").withTarget(type).persist().build();
        return new UpgradeRecipe(null, upgrade, List.of(), "Something",
            List.of(new CharacterResourceRecipeEntry(10, ECharacterResource.MANA)));
    }

    @Override
    protected List<UpgradeRecipe> createMultipleObjects() {
        ItemType type = typeRepository.insert(getUniverseName(),
            new ItemType(null, "Test-Type", ETypeRestriction.ITEM));
        Upgrade upgrade1 = createUpgrade().withName("Test 1").withTarget(type).persist().build();
        Upgrade upgrade2 = createUpgrade().withName("Test 2").withTarget(type).persist().build();
        Item item = createItem().persist().buildItem();
        return List.of(new UpgradeRecipe(null, upgrade1, List.of(), "", List.of(new ItemRecipeEntry(2, item))),
            new UpgradeRecipe(null, upgrade2, List.of(upgrade1), "", List.of(new ItemRecipeEntry(10, item))));
    }
}