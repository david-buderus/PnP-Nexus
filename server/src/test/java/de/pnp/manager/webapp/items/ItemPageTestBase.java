package de.pnp.manager.webapp.items;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.UiTestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.utils.TestItemBuilder.TestItemBuilderFactory;
import de.pnp.manager.webapp.pages.ItemPage;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.components.ItemCreation;
import de.pnp.manager.webapp.pages.components.ItemCreation.EItemClass;
import de.pnp.manager.webapp.pages.components.ItemEdit;
import de.pnp.manager.webapp.pages.components.OverviewTable;
import de.pnp.manager.webapp.pages.components.OverviewTable.OverviewTableRows;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for testing item overview pages.
 */
@TestServer(EServerTestConfiguration.BASIC_ITEMS)
@UiTestServer
public abstract class ItemPageTestBase extends ServerTestBase {

    /**
     * The {@link ItemRepository}.
     */
    @Autowired
    protected ItemRepository itemRepository;

    /**
     * An item builder for the tests.
     */
    @Autowired
    protected TestItemBuilderFactory itemBuilder;

    /**
     * The page which gets tested.
     */
    protected ItemPage page;

    /**
     * The test universe.
     */
    protected Universe universe;

    @BeforeEach
    void openItemPage() {
        universe = getUniverse();
        page = openTestPage(webDriver.openMainMenu("admin", "admin"));
        page.selectActiveUniverse(universe);
    }

    @Test
    void testSorting() {
        Collection<Item> items = getTestItems();

        OverviewTable table = page.getTable();

        assertSorting(table, items, Comparator.comparing(Item::getName));

        table.clickSortBy("Name");
        assertSorting(table, items, Comparator.comparing(Item::getName).reversed());

        table.clickSortBy("Subtype");
        assertSorting(table, items, Comparator.comparing(item -> item.getSubtype().getName()));
    }

    @Test
    void testDeleting() {
        Collection<Item> items = getTestItems();
        Item item = items.stream().min(Comparator.comparing(Item::getName)).orElseThrow();

        OverviewTable table = page.getTable();

        Assertions.assertThat(page.isDeleteDisabled()).isTrue();

        table.getTableRow(item).select();

        page.deleteSelectedItems();

        if (items.size() > 1) {
            assertThat(table.getAllTableRows().asLocator().first()).hasCount(1);
        } else {
            assertThat(table.getAllTableRows().asLocator()).hasCount(0);
        }
        assertThat(table.getTableRow(item).asLocator()).hasCount(0);
        Assertions.assertThat(itemRepository.get(universe.getName(), item.getId())).isEmpty();

        Assertions.assertThat(page.isDeleteDisabled()).isTrue();
    }

    @Test
    void testAdd() {
        ItemCreation creation = page.openItemAddMenu();
        OverviewTable table = page.getTable();

        Pair<EItemClass, Item> testItem = getTestItem();
        EItemClass itemClass = testItem.getLeft();
        Item item = testItem.getRight();

        creation.setItemClass(itemClass);
        creation.setItem(item);

        assertThat(creation.getResultingPrice()).isNotBlank();

        creation.setName("");
        creation.addItem();

        assertThat(creation.getNameError()).isNotBlank();

        creation.setName(item.getName());
        creation.addItem();

        assertThat(table.asLocator()).containsText(item.getName());

        Optional<Item> persistedItem = itemRepository.get(universe.getName(), item.getName());
        Assertions.assertThat(persistedItem).isPresent();
        Assertions.assertThat(persistedItem).contains(item);

        assertThat(table.getTableRow(persistedItem.get()).asLocator()).hasCount(1);
    }

    @Test
    void testEdit() {
        OverviewTable table = page.getTable();

        Collection<Item> items = getTestItems();
        Item item = items.stream().min(Comparator.comparing(Item::getName)).orElseThrow();

        Assertions.assertThat(page.isEditDisabled()).isTrue();
        table.getTableRow(item).select();
        Assertions.assertThat(page.isEditDisabled()).isFalse();

        ItemEdit edit = page.openItemEditMenu();

        edit.setTier(-1);
        edit.editItem();

        Assertions.assertThat(edit.getTierError()).isNotBlank();

        edit.setTier(4);
        edit.editItem();

        assertThat(table.getTableRow(item).asLocator()).containsText("4");

        Optional<Item> persistedItem = itemRepository.get(universe.getName(), item.getId());
        Assertions.assertThat(persistedItem).isPresent();
        Assertions.assertThat(persistedItem.get().getTier()).isEqualTo(4);

        assertThat(table.getTableRow(persistedItem.get()).asLocator()).hasCount(1);
    }

    /**
     * Assert the sorting of the table.
     */
    protected static void assertSorting(OverviewTable table, Collection<Item> items, Comparator<Item> comparator) {
        List<Item> sorted = items.stream().sorted(comparator).toList();

        OverviewTableRows tableRows = table.getAllTableRows();
        for (int i = 0; i < sorted.size(); i++) {
            assertThat(tableRows.getRow(i).getAllTableCells().nth(1)).hasText(sorted.get(i).getName());
        }
    }

    /**
     * Opens the page which should get tested.
     */
    protected abstract ItemPage openTestPage(MainMenu mainMenu);

    /**
     * Returns all items which should be shown in the table.
     */
    protected abstract Collection<Item> getTestItems();

    /**
     * Returns a test item which should be added.
     */
    protected abstract Pair<EItemClass, Item> getTestItem();
}
