package de.pnp.manager.webapp.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.webapp.pages.components.ItemCreation;
import de.pnp.manager.webapp.pages.components.ItemEdit;
import de.pnp.manager.webapp.pages.components.OverviewTable;

/**
 * Represents the item overview page.
 */
public class ItemPage extends PageBase {

    public ItemPage(Page page) {
        super(page);
    }

    /**
     * Returns the table of the page.
     */
    public OverviewTable getTable() {
        return OverviewTable.getOverviewTable(page);
    }

    /**
     * Tries to delete all selected items.
     */
    public void deleteSelectedItems() {
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete").setExact(true)).click();
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Confirm")).click();
    }

    /**
     * Checks if the delete button is disabled.
     */
    public boolean isDeleteDisabled() {
        return page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Delete").setExact(true)).isDisabled();
    }

    /**
     * Opens the item add menu.
     */
    public ItemCreation openItemAddMenu() {
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Add").setExact(true)).click();
        return ItemCreation.getItemCreation(page);
    }

    /**
     * Tries to open the item edit menu.
     */
    public ItemEdit openItemEditMenu() {
        page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Edit").setExact(true)).click();
        return ItemEdit.getItemEdit(page);
    }

    /**
     * Checks if the edit button is disabled.
     */
    public boolean isEditDisabled() {
        return page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Edit").setExact(true)).isDisabled();
    }
}
