package de.pnp.manager.webapp.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.webapp.pages.components.DatabaseObjectDialog;
import de.pnp.manager.webapp.pages.components.OverviewTable;

/**
 * Describes the overview base page
 */
public class OverviewBasePage extends PageBase {

    public OverviewBasePage(Page page) {
        super(page);
    }

    /**
     * Returns the table of the page.
     */
    public OverviewTable getTable() {
        return OverviewTable.getOverviewTable(page);
    }

    /**
     * Opens the item add menu.
     */
    public DatabaseObjectDialog openAddDialog() {
        page.getByTestId("add").click();
        return DatabaseObjectDialog.getDialog(page);
    }

    /**
     * Tries to open the edit menu.
     */
    public DatabaseObjectDialog openEditDialog() {
        page.getByTestId("edit").click();
        return DatabaseObjectDialog.getDialog(page);
    }

    /**
     * Tries to delete all selected objects.
     */
    public void deleteSelectedObjects() {
        page.getByTestId("delete").click();
        page.getByRole(AriaRole.DIALOG).locator("[type=submit]").click();
    }

    /**
     * Checks if the edit button is disabled.
     */
    public boolean isEditDisabled() {
        return page.getByTestId("edit").isDisabled();
    }

    /**
     * Checks if the delete button is disabled.
     */
    public boolean isDeleteDisabled() {
        return page.getByTestId("delete").isDisabled();
    }
}
