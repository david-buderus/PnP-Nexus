package de.pnp.manager.webapp.database;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.UiTestServer;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import de.pnp.manager.webapp.pages.components.DatabaseObjectDialog;
import de.pnp.manager.webapp.pages.components.OverviewTable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Base class for testing overview pages.
 */
@UiTestServer
public abstract class OverviewTestBase<T extends DatabaseObject> extends ServerTestBase {

    /**
     * The page which gets tested.
     */
    protected OverviewBasePage page;

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
        Collection<T> objects = getTestObjects();

        OverviewTable table = page.getTable();

        table.assertIsSorted(objects, getDefaultSort());

        for (Pair<String, Comparator<T>> sorter : getSorters()) {
            table.clickSortBy(sorter.getLeft());
            table.assertIsSorted(objects, sorter.getRight());
        }
    }

    @Test
    void testAdd() {
        OverviewTable table = page.getTable();
        DatabaseObjectDialog dialog = page.openAddDialog();

        dialog.fillOut(getWrongObject());
        dialog.add();

        for (String field : getExpectedErrorFields()) {
            dialog.assertError(field);
        }

        T correctObject = getCorrectObject();
        dialog.fillOut(correctObject);
        dialog.add();

        assertThat(table.asLocator()).containsText(getIdentifier(correctObject));

        Optional<T> persistedItem = getPersistedObject(correctObject);
        Assertions.assertThat(persistedItem).isPresent();
        Assertions.assertThat(persistedItem).contains(correctObject);

        assertThat(table.getTableRow(persistedItem.get()).asLocator()).hasCount(1);
    }

    @Test
    void testEdit() {
        OverviewTable table = page.getTable();

        Assertions.assertThat(page.isEditDisabled()).isTrue();
        table.getTableRow(getModifyId().toHexString()).select();
        Assertions.assertThat(page.isEditDisabled()).isFalse();

        DatabaseObjectDialog dialog = page.openEditDialog();

        T editedObject = getEditedObject();
        dialog.fillOut(editedObject);
        dialog.edit();

        assertThat(table.getTableRow(getModifyId()).asLocator()).containsText(getChangeIdentifier());

        Optional<T> persistedItem = getPersistedObject(editedObject);
        Assertions.assertThat(persistedItem).isPresent();
        Assertions.assertThat(persistedItem).contains(editedObject);

        assertThat(table.getTableRow(persistedItem.get()).asLocator()).hasCount(1);
    }

    @Test
    void testDelete() {
        OverviewTable table = page.getTable();
        Collection<T> testObjects = getTestObjects();
        ObjectId modifyId = getModifyId();

        Assertions.assertThat(page.isDeleteDisabled()).isTrue();
        table.getTableRow(modifyId).select();
        Assertions.assertThat(page.isDeleteDisabled()).isFalse();

        page.deleteSelectedObjects();

        if (testObjects.size() > 1) {
            assertThat(table.getAllTableRows().asLocator().first()).hasCount(1);
        } else {
            assertThat(table.getAllTableRows().asLocator()).hasCount(0);
        }
        assertThat(table.getTableRow(modifyId).asLocator()).hasCount(0);

        Assertions.assertThat(getPersistedObject(modifyId)).isEmpty();
    }

    /**
     * Opens the page which should get tested.
     */
    protected abstract OverviewBasePage openTestPage(MainMenu mainMenu);

    /**
     * Returns all known test objects.
     */
    protected abstract Collection<T> getTestObjects();

    /**
     * Returns the default sort of the underlying table.
     */
    protected abstract Comparator<T> getDefaultSort();

    /**
     * Returns sorters which should get tested with their corresponding id.
     */
    protected abstract List<Pair<String, Comparator<T>>> getSorters();

    /**
     * Returns an object which will have validation errors.
     *
     * @see #getExpectedErrorFields()
     */
    protected abstract T getWrongObject();

    /**
     * Returns a list of all field which should have a validation error.
     *
     * @see #getWrongObject()
     */
    protected abstract List<String> getExpectedErrorFields();

    /**
     * Returns an object without validation errors.
     */
    protected abstract T getCorrectObject();

    /**
     * Returns an identifier with which the element can be found in the table.
     *
     * @see IUniquelyNamedDataObject#getName()
     */
    protected abstract String getIdentifier(T object);

    /**
     * Returns the persisted counterpart of the given object.
     *
     * @see DatabaseObject#isPersisted()
     */
    protected abstract Optional<T> getPersistedObject(T object);

    /**
     * Returns the persisted object given the ID.
     */
    protected abstract Optional<T> getPersistedObject(ObjectId id);

    /**
     * ID of the object which will get modified during the tests.
     */
    protected abstract ObjectId getModifyId();

    /**
     * How the modified object should look like after the edit.
     */
    protected abstract T getEditedObject();

    /**
     * A text which will be found in the table only after the object got edited.
     */
    protected abstract String getChangeIdentifier();
}
