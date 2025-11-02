package de.pnp.manager.webapp.items;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.EArmorSlot;
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
public class ArmorPageTest extends UniquelyNamedOverviewTestBase<Item, ItemRepository> {

    @Autowired
    private MaterialRepository materialRepository;

    protected ArmorPageTest(@Autowired ItemRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openArmorPage();
    }

    @Override
    protected Armor getWrongObject() {
        return createItemBuilder().withName("").withVendorPrice(-1).withUpgradeSlots(-1).buildArmor();
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "vendorPrice", "material", "upgradeSlots");
    }

    @Override
    protected Armor getCorrectObject() {
        Material iron = materialRepository.get(getUniverseName(), "Iron").orElseThrow();
        return createItemBuilder().withName("Piece of Wood").withMaterial(iron).withArmor(3)
                .withVendorPrice(154).withRarity(ERarity.EPIC).withArmorSlot(EArmorSlot.ARMS).buildArmor();
    }

    @Override
    protected Armor getEditedObject() {
        Armor armor = (Armor) getOriginalModifyObject();
        return new Armor(null, "Other Helmet", armor.getTags(), armor.getRequirement(), armor.getEffect(),
                armor.getRarity(), 302, armor.getTier(), "A fancy helmet", armor.getNote(),
                armor.getMaterial(), armor.getUpgradeSlots(), armor.getArmorSlot(), armor.getArmor(),
                armor.getProtection(), armor.getWeight(), armor.getMaximumStackSize(), armor.getMinimumStackSize());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Other Helmet";
    }

    @Override
    protected String getEditObjectName() {
        return "Iron Helmet";
    }
}
