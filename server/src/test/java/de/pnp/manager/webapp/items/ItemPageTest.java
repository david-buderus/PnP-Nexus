package de.pnp.manager.webapp.items;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.webapp.database.UniquelyNamedOverviewTestBase;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static de.pnp.manager.utils.TestItemBuilder.createItemBuilder;

/**
 * Tests the item overview page.
 */
@TestServer(EServerTestConfiguration.BASIC_ITEMS)
public class ItemPageTest extends UniquelyNamedOverviewTestBase<Item, ItemRepository> {

    protected ItemPageTest(@Autowired ItemRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openItemPage();
    }

    @Override
    protected Item getWrongObject() {
        return createItemBuilder().withName("").withVendorPrice(-1).buildItem();
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "vendorPrice");
    }

    @Override
    protected Item getCorrectObject() {
        return createItemBuilder().withName("Apple").withVendorPrice(154).withRarity(ERarity.EPIC).buildItem();
    }

    @Override
    protected Item getEditedObject() {
        Item item = getOriginalModifyObject();
        return new Item(null, item.getName(), item.getTags(), item.getRequirement(), item.getEffects(),
                item.getRarity(), 302, item.getTier(), "A raw piece of wood", item.getNote(),
                item.getMaximumStackSize(), item.getMinimumStackSize());
    }

    @Override
    protected String getChangeIdentifier() {
        return "A raw piece of wood";
    }

    @Override
    protected String getEditObjectName() {
        return "Raw Wood";
    }
}
