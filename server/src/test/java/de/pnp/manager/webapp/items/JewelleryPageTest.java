package de.pnp.manager.webapp.items;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.webapp.pages.ItemPage;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.components.ItemCreation.EItemClass;
import java.util.Collection;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Tests the item overview page.
 */
public class JewelleryPageTest extends ItemPageTestBase {

    @Override
    protected ItemPage openTestPage(MainMenu mainMenu) {
        return mainMenu.openJewelleryPage();
    }

    @Override
    protected Collection<Item> getTestItems() {
        return itemRepository.getAll(universe.getName()).stream().filter(Jewellery.class::isInstance).toList();
    }

    @Override
    protected Pair<EItemClass, Item> getTestItem() {
        return ImmutablePair.of(EItemClass.JEWELLERY,
            itemBuilder.createItemBuilder(universe.getName()).withName("Iron Band").withType("Jewellery")
                .withSubtype("Ring")
                .withEffect("It is different").withDescription("It is round").withRarity(ERarity.RARE)
                .withTier(2)
                .withRequirement("None").withVendorPrice(10000)
                .withNote("Some text").withMaterial("Iron").withUpgradeSlots(2).buildJewellery());
    }
}
