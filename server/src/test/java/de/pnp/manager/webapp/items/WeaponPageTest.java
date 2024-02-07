package de.pnp.manager.webapp.items;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.webapp.pages.ItemPage;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.components.ItemCreation.EItemClass;
import java.util.Collection;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Tests the item overview page.
 */
public class WeaponPageTest extends ItemPageTestBase {

    @Override
    protected ItemPage openTestPage(MainMenu mainMenu) {
        return mainMenu.openWeaponPage();
    }

    @Override
    protected Collection<Item> getTestItems() {
        return itemRepository.getAll(universe.getName()).stream().filter(Weapon.class::isInstance).toList();
    }

    @Override
    protected Pair<EItemClass, Item> getTestItem() {
        return ImmutablePair.of(EItemClass.WEAPON,
            itemBuilder.createItemBuilder(universe.getName()).withName("Iron Great-Sword").withType("Weapon")
                .withSubtype("Sword")
                .withEffect("It is great").withDescription("It is a great sword").withRarity(ERarity.UNCOMMON)
                .withTier(2)
                .withRequirement("None").withVendorPrice(10000)
                .withNote("Some text").withMaterial("Iron").withDice("D6").withDamage(2).withUpgradeSlots(2).withHit(0)
                .withInitiative(1).buildWeapon());
    }
}
