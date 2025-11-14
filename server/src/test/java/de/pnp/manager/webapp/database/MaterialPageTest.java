package de.pnp.manager.webapp.database;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.Material.MaterialItem;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Tests the material overview page.
 */
@TestServer(EServerTestConfiguration.BASIC_ITEMS)
public class MaterialPageTest extends UniquelyNamedOverviewTestBase<Material, MaterialRepository> {

    @Autowired
    private ItemRepository itemRepository;

    protected MaterialPageTest(@Autowired MaterialRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openMaterialPage();
    }

    @Override
    protected Material getWrongObject() {
        return new Material(null, null, List.of(new MaterialItem(0, null)));
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "items.0.amount");
    }

    @Override
    protected Material getCorrectObject() {
        return new Material(null, "Blood", List.of(new MaterialItem(0.2, getItem("Iron Ore"))));
    }

    @Override
    protected String getEditObjectName() {
        return "Iron";
    }

    @Override
    protected Material getEditedObject() {
        return new Material(null, "Iron", List.of(new MaterialItem(10, getItem("Iron Ore"))));
    }

    @Override
    protected String getChangeIdentifier() {
        return "Iron Ore";
    }

    private Item getItem(String name) {
        return itemRepository.get(getUniverseId(), name).orElseThrow();
    }
}
