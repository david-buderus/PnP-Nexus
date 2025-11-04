package de.pnp.manager.webapp.pages;

import com.microsoft.playwright.Page;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.webapp.pages.components.Select;

/**
 * Base class for testing webapp pages.
 */
public class PageBase {

    /**
     * The underlying {@link Page}.
     */
    protected final Page page;

    public PageBase(Page page) {
        this.page = page;
    }

    /**
     * Select the currently active {@link Universe}.
     */
    public void selectActiveUniverse(Universe universe) {
        Select.from(page.getByTestId("universe-selector")).select(universe.getName(), false);
    }

    /**
     * Goes back to the {@link MainMenu}
     */
    public MainMenu toMainMenu() {
        page.getByTestId("main-menu").click();
        return new MainMenu(page);
    }

    /**
     * Returns the underlying {@link Page}.
     */
    public Page asPage() {
        return page;
    }
}
