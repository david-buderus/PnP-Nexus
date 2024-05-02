package de.pnp.manager.webapp.pages.components;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.LocatorOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.component.DatabaseObject;

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
     * Clicks the sorting button of the table column with the given label.
     */
    public void clickSortBy(String label) {
        table.locator("//thead").locator("//th", new LocatorOptions().setHasText(label)).getByRole(AriaRole.BUTTON)
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
         * Returns the underlying {@link Locator}.
         */
        public Locator asLocator() {
            return row;
        }
    }
}
