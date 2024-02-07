package de.pnp.manager.webapp.items;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.webapp.pages.ItemPage;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.components.ItemCreation.EItemClass;
import java.util.Collection;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Tests the item overview page.
 */
public class ShieldPageTest extends ItemPageTestBase {

    @Override
    protected ItemPage openTestPage(MainMenu mainMenu) {
        return mainMenu.openShieldPage();
    }

    @Override
    protected Collection<Item> getTestItems() {
        return itemRepository.getAll(universe.getName()).stream().filter(Shield.class::isInstance).toList();
    }

    @Override
    protected Pair<EItemClass, Item> getTestItem() {
        return ImmutablePair.of(EItemClass.SHIELD,
            itemBuilder.createItemBuilder(universe.getName()).withName("Iron Giant Shield").withType("Armor")
                .withSubtype("Shield")
                .withEffect("It is great").withDescription("It is a giant shield").withRarity(ERarity.LEGENDARY)
                .withTier(2)
                .withRequirement("None").withVendorPrice(10000)
                .withNote("Some text").withMaterial("Iron").withUpgradeSlots(2).withHit(0)
                .withInitiative(-1).withArmor(100).withWeight(10).buildShield());
    }
}
