package de.pnp.manager.webapp.pages.species;

import com.microsoft.playwright.Page;
import de.pnp.manager.webapp.pages.components.species.NationFormPage;

/**
 * Page object for the nation detail page
 */
public class NationDetailPage extends DetailPageBase {

    public NationDetailPage(Page page) {
        super(page);
    }

    /**
     * Opens the species creation form.
     */
    public NationFormPage edit() {
        page.getByTestId("edit").click();
        return new NationFormPage(page);
    }
}
