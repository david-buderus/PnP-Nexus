package de.pnp.manager.webapp.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;

/**
 * Represents the dropdown button
 */
public class DropdownButton extends ComponentBase {

    protected DropdownButton(Locator locator) {
        super(locator);
    }

    /**
     * Creates the component
     */
    public static DropdownButton from(Locator locator) {
        return new DropdownButton(locator);
    }

    /**
     * Clicks the main button.
     */
    public void click() {
        locator.getByTestId("main-button").click();
    }

    /**
     * Clicks the dropdown menu button.
     */
    public void clickDropdown(String dataTestId) {
        locator.getByTestId("dropdownMenu").click();
        locator.page().getByRole(AriaRole.MENUITEM).and(locator.page().getByTestId(dataTestId)).click();
    }
}
