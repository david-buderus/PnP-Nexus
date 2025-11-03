package de.pnp.manager.webapp.pages.components;

import com.microsoft.playwright.Locator;

/**
 * A base for all components.
 */
public abstract class ComponentBase {

    /**
     * The locator of this base
     */
    protected final Locator locator;

    protected ComponentBase(Locator locator) {
        this.locator = locator;
    }

    /**
     * Returns the underlying locator.
     */
    public Locator asLocator() {
        return locator;
    }
}
