package de.pnp.manager.webapp.items;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.webapp.pages.ItemPage;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.components.ItemCreation.EItemClass;
import java.util.Collection;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Tests the item overview page.
 */
public class ItemPageTest extends ItemPageTestBase {

    @Override
    protected ItemPage openTestPage(MainMenu mainMenu) {
        return mainMenu.openItemPage();
    }

    @Override
    protected Collection<Item> getTestItems() {
        return itemRepository.getAll(universe.getName());
    }

    @Override
    protected Pair<EItemClass, Item> getTestItem() {
        return ImmutablePair.of(EItemClass.ITEM,
            itemBuilder.createItemBuilder(universe.getName()).withName("Silver Ore").withType("Material")
                .withSubtype("Ore")
                .withEffect("It is shiny").withDescription("It is silver ore").withRarity(ERarity.UNCOMMON).withTier(2)
                .withRequirement("None").withVendorPrice(10000).withMinimumStackSize(10).withMaximumStackSize(50)
                .withNote("Some text").buildItem());
    }
}
