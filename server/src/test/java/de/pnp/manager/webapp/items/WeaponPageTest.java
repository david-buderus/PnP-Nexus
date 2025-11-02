package de.pnp.manager.webapp.items;

import de.pnp.manager.component.Dice;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.webapp.database.UniquelyNamedOverviewTestBase;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static de.pnp.manager.utils.TestItemBuilder.createItemBuilder;

/**
 * Tests the weapon overview page.
 */
@TestServer(EServerTestConfiguration.BASIC_ITEMS)
public class WeaponPageTest extends UniquelyNamedOverviewTestBase<Item, ItemRepository> {

    @Autowired
    private MaterialRepository materialRepository;

    protected WeaponPageTest(@Autowired ItemRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openWeaponPage();
    }

    @Override
    protected Weapon getWrongObject() {
        return createItemBuilder().withName("").withVendorPrice(-1).withUpgradeSlots(-1).buildWeapon();
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "vendorPrice", "material", "upgradeSlots");
    }

    @Override
    protected Weapon getCorrectObject() {
        Material iron = materialRepository.get(getUniverseName(), "Iron").orElseThrow();
        return createItemBuilder().withName("The Stick").withMaterial(iron).withDice(Dice.simpleDice(6))
                .withVendorPrice(154).withRarity(ERarity.EPIC).buildWeapon();
    }

    @Override
    protected Weapon getEditedObject() {
        Weapon weapon = (Weapon) getOriginalModifyObject();
        return new Weapon(null, "Greatsword", weapon.getTags(), weapon.getRequirement(), weapon.getEffect(),
                weapon.getRarity(), 302, weapon.getTier(), "A big weapon", weapon.getNote(),
                weapon.getMaterial(), weapon.getUpgradeSlots(), weapon.getInitiative(), weapon.getHit(),
                weapon.getDamage(), weapon.getDice(), weapon.getMaximumStackSize(), weapon.getMinimumStackSize());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Greatsword";
    }

    @Override
    protected String getEditObjectName() {
        return "Iron Sword";
    }
}
