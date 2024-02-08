package de.pnp.manager.webapp.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import de.pnp.manager.webapp.utils.WebTestUtils;

/**
 * Represents the ItemCreation component.
 */
public class ItemCreation extends ItemManipulation {

    private ItemCreation(Locator locator) {
        super(locator);
    }

    /**
     * Creates the ItemCreation from the given {@link Page}.
     */
    public static ItemCreation getItemCreation(Page page) {
        return new ItemCreation(page.getByTestId("item-creation-dialog"));
    }

    /**
     * Sets the class of the item which will be created.
     */
    public void setItemClass(EItemClass itemClass) {
        WebTestUtils.select(locator.getByTestId("item-class-select"), itemClass.value);
    }

    /**
     * Tries to add the item to the universe.
     */
    public void addItem() {
        locator.getByTestId("item-add").click();
    }

    /**
     * Possible item classes which can be created.
     */
    public enum EItemClass {
        ITEM("Item"), JEWELLERY("Jewellery"), WEAPON("Weapon"), ARMOR("Armor"), SHIELD("Shield");

        private final String value;

        EItemClass(String value) {
            this.value = value;
        }
    }
}
