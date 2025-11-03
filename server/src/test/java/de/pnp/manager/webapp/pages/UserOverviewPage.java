package de.pnp.manager.webapp.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.webapp.pages.components.OverviewTable;
import de.pnp.manager.webapp.pages.components.users.UserCreation;
import de.pnp.manager.webapp.pages.components.users.UserEdit;

/**
 * Represents the user overview page.
 */
public class UserOverviewPage extends PageBase {

    public UserOverviewPage(Page page) {
        super(page);
    }

    /**
     * Returns the table of the page.
     */
    public OverviewTable getTable() {
        return OverviewTable.getOverviewTable(page);
    }

    /**
     * Tries to delete all selected users.
     */
    public void deleteSelectedUsers() {
        page.getByTestId("delete").click();
        page.getByRole(AriaRole.DIALOG).locator("[type=submit]").click();
    }

    /**
     * Checks if the delete button is disabled.
     */
    public boolean isDeleteDisabled() {
        return page.getByTestId("delete").isDisabled();
    }

    /**
     * Opens the item add menu.
     */
    public UserCreation openUserAddMenu() {
        page.getByTestId("add").click();
        return UserCreation.getUserCreation(page);
    }

    /**
     * Tries to open the item edit menu.
     */
    public UserEdit openUserEditMenu() {
        page.getByTestId("edit").click();
        return UserEdit.getUserEdit(page);
    }

    /**
     * Checks if the edit button is disabled.
     */
    public boolean isEditDisabled() {
        return page.getByTestId("edit").isDisabled();
    }
}
