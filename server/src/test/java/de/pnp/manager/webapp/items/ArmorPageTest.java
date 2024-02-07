package de.pnp.manager.webapp.items;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.webapp.pages.ItemPage;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.components.ItemCreation.EItemClass;
import java.util.Collection;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Tests the item overview page.
 */
public class ArmorPageTest extends ItemPageTestBase {

    @Override
    protected ItemPage openTestPage(MainMenu mainMenu) {
        return mainMenu.openArmorPage();
    }

    @Override
    protected Collection<Item> getTestItems() {
        return itemRepository.getAll(universe.getName()).stream().filter(Armor.class::isInstance).toList();
    }

    @Override
    protected Pair<EItemClass, Item> getTestItem() {
        return ImmutablePair.of(EItemClass.ARMOR,
            itemBuilder.createItemBuilder(universe.getName()).withName("Iron Spike Helmet").withType("Armor")
                .withSubtype("Head")
                .withEffect("It is spiky").withDescription("It is a spiky helmet").withRarity(ERarity.EPIC)
                .withTier(2)
                .withRequirement("None").withVendorPrice(10000)
                .withNote("Some text").withMaterial("Iron").withUpgradeSlots(2).withArmor(100).withWeight(10)
                .buildArmor());
    }
}
