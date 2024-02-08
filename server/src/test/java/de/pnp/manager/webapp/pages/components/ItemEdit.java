package de.pnp.manager.webapp.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Represents the ItemEdit component.
 */
public class ItemEdit extends ItemManipulation {

    private ItemEdit(Locator locator) {
        super(locator);
    }

    /**
     * Creates the ItemEdit from the given {@link Page}.
     */
    public static ItemEdit getItemEdit(Page page) {
        return new ItemEdit(page.getByTestId("item-edit-dialog"));
    }

    /**
     * Tries to edit the item in the universe.
     */
    public void editItem() {
        locator.getByTestId("item-edit").click();
    }
}
