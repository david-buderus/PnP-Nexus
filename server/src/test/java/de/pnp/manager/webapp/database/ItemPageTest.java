package de.pnp.manager.webapp.database;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the item overview page.
 */
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
        return null;
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return null;
    }

    @Override
    protected Item getCorrectObject() {
        return null;
    }

    @Override
    protected Item getEditedObject() {
        return null;
    }

    @Override
    protected String getChangeIdentifier() {
        return null;
    }

    @Override
    protected String getEditObjectName() {
        return null;
    }
}
