package de.pnp.manager.webapp.items;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Shield;
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
public class ShieldPageTest extends UniquelyNamedOverviewTestBase<Item, ItemRepository> {

    @Autowired
    private MaterialRepository materialRepository;

    protected ShieldPageTest(@Autowired ItemRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openShieldPage();
    }

    @Override
    protected Shield getWrongObject() {
        return createItemBuilder().withName("").withVendorPrice(-1).withUpgradeSlots(-1).buildShield();
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "vendorPrice", "material", "upgradeSlots");
    }

    @Override
    protected Shield getCorrectObject() {
        Material iron = materialRepository.get(getUniverseId(), "Iron").orElseThrow();
        return createItemBuilder().withName("Piece of Wood").withMaterial(iron).withArmor(3)
                .withVendorPrice(154).withRarity(ERarity.EPIC).buildShield();
    }

    @Override
    protected Shield getEditedObject() {
        Shield shield = (Shield) getOriginalModifyObject();
        return new Shield(null, "Towershield", shield.getTags(), shield.getRequirement(), shield.getEffect(),
                shield.getRarity(), 302, shield.getTier(), "A big shield", shield.getNote(),
                shield.getMaterial(), shield.getUpgradeSlots(), shield.getInitiative(), shield.getHit(),
                shield.getDice(), shield.getWeight(), shield.getArmor(), shield.getProtection(),
                shield.getMaximumStackSize(), shield.getMinimumStackSize());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Towershield";
    }

    @Override
    protected String getEditObjectName() {
        return "Iron Shield";
    }
}
