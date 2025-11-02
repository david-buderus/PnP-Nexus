package de.pnp.manager.webapp.pages.components;

import com.microsoft.playwright.Locator;

/**
 * A switch from Mantine
 */
public class Switch extends ComponentBase {

    private Switch(Locator locator) {
        super(locator);
    }

    /**
     * Returns a switch defined by the given locator.
     */
    public static Switch from(Locator locator) {
        // The Mantine switch input is invisible. We need to use the wrapper around it
        return new Switch(locator.locator(".."));
    }

    /**
     * Sets the state of this switch.
     */
    public void set(boolean value) {
        if (value) {
            locator.check();
        } else {
            locator.uncheck();
        }
    }
}
