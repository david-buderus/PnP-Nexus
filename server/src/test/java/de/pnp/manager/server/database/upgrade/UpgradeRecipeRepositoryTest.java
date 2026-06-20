package de.pnp.manager.server.database.upgrade;

import de.pnp.manager.component.IResourceUsage.CharacterResourceUsage;
import de.pnp.manager.component.IResourceUsage.ItemUsage;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.UpgradeRecipe;
import de.pnp.manager.component.upgrade.effect.SimpleItemEffect;
import de.pnp.manager.server.database.RepositoryTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Tests for {@link UpgradeRecipeRepository}.
 */
class UpgradeRecipeRepositoryTest extends RepositoryTestBase<UpgradeRecipe, UpgradeRecipeRepository> {

    @Autowired
    private UpgradeRepository upgradeRepository;

    public UpgradeRecipeRepositoryTest(@Autowired UpgradeRecipeRepository repository) {
        super(repository);
    }

    @Test
    void testUpgradeLink() {
        SecondaryAttribute resource = createSecondaryAttribute().withName("Mana").isConsumable().persist().build();
        Upgrade upgradeA = createUpgrade().withName("Shine A")
                .addEffect(new SimpleItemEffect("The weapon emits light")).persist().build();
        Upgrade upgradeB = createUpgrade().withName("Shine B")
                .addEffect(new SimpleItemEffect("The weapon emits light")).build();
        UpgradeRecipe recipe = new UpgradeRecipe(null, upgradeA, List.of(), "",
                List.of(new CharacterResourceUsage(1, resource)));

        testRepositoryLink(UpgradeRecipe::getUpgrade, upgradeRepository, recipe, upgradeA, upgradeB);
    }

    @Test
    void testNecessaryUpgradeLink() {
        SecondaryAttribute resource = createSecondaryAttribute().withName("Mana").isConsumable().persist().build();
        Upgrade result = createUpgrade().withName("Result").persist().build();
        Upgrade upgradeA = createUpgrade().withName("Shine A")
                .addEffect(new SimpleItemEffect("The weapon emits light")).persist().build();

        Upgrade upgradeB = createUpgrade().withName("Shine B")
                .addEffect(new SimpleItemEffect("The weapon emits a lot of light")).build();
        UpgradeRecipe recipe = new UpgradeRecipe(null, result, List.of(upgradeA), "",
                List.of(new CharacterResourceUsage(100, resource)));

        testRepositoryCollectionLink(UpgradeRecipe::getRequiredUpgrades, upgradeRepository, recipe, List.of(upgradeA),
                Map.of(upgradeA, upgradeB));
    }

    @Override
    protected UpgradeRecipe createObject() {
        SecondaryAttribute resource = createSecondaryAttribute().withName("Mana").isConsumable().persist().build();
        Upgrade upgrade = createUpgrade().withName("Test").withNecessaryTags("Test-Tag").persist().build();
        return new UpgradeRecipe(null, upgrade, List.of(), "",
                List.of(new CharacterResourceUsage(10, resource)));
    }

    @Override
    protected UpgradeRecipe createSlightlyChangeObject() {
        SecondaryAttribute resource = createSecondaryAttribute().withName("Life").isConsumable().persist().build();
        Upgrade upgrade = createUpgrade().withName("Other Test").withNecessaryTags("Other-Test-Tag").persist().build();
        return new UpgradeRecipe(null, upgrade, List.of(), "Something",
                List.of(new CharacterResourceUsage(10, resource)));
    }

    @Override
    protected List<UpgradeRecipe> createMultipleObjects() {
        Upgrade upgrade1 = createUpgrade().withName("Test 1").persist().build();
        Upgrade upgrade2 = createUpgrade().withName("Test 2").persist().build();
        Item item = createItem().persist().buildItem();
        return List.of(new UpgradeRecipe(null, upgrade1, List.of(), "", List.of(new ItemUsage(2, item))),
                new UpgradeRecipe(null, upgrade2, List.of(upgrade1), "", List.of(new ItemUsage(10, item))));
    }
}