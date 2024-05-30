package de.pnp.manager.webapp.pages.components;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.LocatorOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.component.DatabaseObject;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;

/**
 * Represents the OverviewTable component.
 */
public class OverviewTable {

    private final Locator base;
    private final Locator table;

    public OverviewTable(Locator base) {
        this.base = base;
        this.table = base.getByRole(AriaRole.TABLE);
    }

    /**
     * Returns the {@link OverviewTable} of a page.
     */
    public static OverviewTable getOverviewTable(Page page) {
        return new OverviewTable(page.getByTestId("overview-table"));
    }

    /**
     * Returns all table rows.
     */
    public OverviewTableRows getAllTableRows() {
        return new OverviewTableRows(table.locator("//tbody").locator("//tr"));
    }

    /**
     * Returns the table row matching the given {@link DatabaseObject}.
     */
    public OverviewTableRows getTableRow(DatabaseObject object) {
        return getTableRow(object.getId().toHexString());
    }

    /**
     * Returns the table row matching the given string.
     */
    public OverviewTableRows getTableRow(String id) {
        return new OverviewTableRows(table.locator("//tbody").getByTestId(id));
    }

    /**
     * Returns the table row matching the given id.
     */
    public OverviewTableRows getTableRow(ObjectId id) {
        return getTableRow(id.toHexString());
    }

    /**
     * Clicks the sorting button of the table column with the given label.
     */
    public void clickSortByLabel(String label) {
        table.locator("//thead").locator("//th", new LocatorOptions().setHasText(label)).getByRole(AriaRole.BUTTON)
            .click();
    }

    /**
     * Clicks the sorting button of the table column with the given test-id.
     */
    public void clickSortBy(String testId) {
        table.locator("//thead").getByTestId(testId).getByRole(AriaRole.BUTTON)
            .click();
    }

    /**
     * Asserts that the table row with the given id exists.
     */
    public void assertThatTableRowExists(String id) {
        assertThat(getTableRow(id).asLocator()).hasCount(1);
    }

    /**
     * Asserts that the table row with the given id does not exist.
     */
    public void assertThatTableRowNotExists(String id) {
        assertThat(getTableRow(id).asLocator()).hasCount(0);
    }

    /**
     * Asserts that the table has x entries.
     */
    public void assertExactlyEntries(int entries) {
        assertThat(getAllTableRows().asLocator().first()).hasCount(entries);
    }

    /**
     * Asserts that the table is sorted
     */
    public <T> void assertIsSorted(Collection<T> objects, Comparator<T> comparator, Function<T, String> id) {
        List<T> sorted = objects.stream().sorted(comparator).toList();

        OverviewTableRows tableRows = getAllTableRows();
        for (int i = 0; i < sorted.size(); i++) {
            String expected = id.apply(sorted.get(i));
            String actual = tableRows.getRow(i).getDataTestId();
            Assertions.assertThat(actual).as("Expected in line %s [%s] but got [%s]", i, expected, actual)
                .isEqualTo(expected);
        }
    }

    /**
     * Asserts that the table is sorted
     */
    public <T extends DatabaseObject> void assertIsSorted(Collection<T> objects, Comparator<T> comparator) {
        assertIsSorted(objects, comparator, object -> object.getId().toHexString());
    }

    /**
     * Returns the underlying {@link Locator}.
     */
    public Locator asLocator() {
        return base;
    }


    /**
     * Represents one or multiple table rows.
     */
    public static class OverviewTableRows {

        private final Locator row;

        private OverviewTableRows(Locator row) {
            this.row = row;
        }

        /**
         * Returns the locator describing all table cells.
         */
        public Locator getAllTableCells() {
            return row.locator("//td");
        }

        /**
         * Selects the given row.
         */
        public void select() {
            getAllTableCells().nth(0).getByRole(AriaRole.CHECKBOX).click();
        }

        /**
         * Returns the nth row if multiple rows a represented.
         */
        public OverviewTableRows getRow(int i) {
            return new OverviewTableRows(row.nth(i));
        }

        /**
         * Returns the associated data testid;
         */
        public String getDataTestId() {
            return row.getAttribute("data-testid");
        }

        /**
         * Returns the underlying {@link Locator}.
         */
        public Locator asLocator() {
            return row;
        }
    }
}
