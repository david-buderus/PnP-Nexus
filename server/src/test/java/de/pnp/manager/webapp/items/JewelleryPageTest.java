package de.pnp.manager.webapp.items;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Jewellery;
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
 * Tests the jewellery overview page.
 */
@TestServer(EServerTestConfiguration.BASIC_ITEMS)
public class JewelleryPageTest extends UniquelyNamedOverviewTestBase<Item, ItemRepository> {

    @Autowired
    private MaterialRepository materialRepository;

    protected JewelleryPageTest(@Autowired ItemRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openJewelleryPage();
    }

    @Override
    protected Jewellery getWrongObject() {
        return createItemBuilder().withName("").withVendorPrice(-1).withUpgradeSlots(-1).buildJewellery();
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "vendorPrice", "material", "upgradeSlots");
    }

    @Override
    protected Jewellery getCorrectObject() {
        Material iron = materialRepository.get(getUniverseName(), "Iron").orElseThrow();
        return createItemBuilder().withName("The One Ring").withMaterial(iron)
                .withVendorPrice(154).withRarity(ERarity.GODLIKE).buildJewellery();
    }

    @Override
    protected Jewellery getEditedObject() {
        Jewellery jewellery = (Jewellery) getOriginalModifyObject();
        return new Jewellery(null, "Another Ring", jewellery.getTags(), jewellery.getRequirement(), jewellery.getEffect(),
                jewellery.getRarity(), 302, jewellery.getTier(), "Just another ring", jewellery.getNote(),
                jewellery.getMaterial(), jewellery.getUpgradeSlots(), jewellery.getMaximumStackSize(),
                jewellery.getMinimumStackSize());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Another Ring";
    }

    @Override
    protected String getEditObjectName() {
        return "Wedding Ring";
    }
}
