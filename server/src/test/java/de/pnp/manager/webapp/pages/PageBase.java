package de.pnp.manager.webapp.pages;

import com.microsoft.playwright.Page;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.webapp.utils.WebTestUtils;

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
        WebTestUtils.selectAutoComplete(page.getByTestId("universe-selector"), universe.getDisplayName(), true);
    }

    /**
     * Returns the underlying {@link Page}.
     */
    public Page asPage() {
        return page;
    }
}
