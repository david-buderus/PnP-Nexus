package de.pnp.manager.webapp.pages.species;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import de.pnp.manager.component.character.Nation;
import de.pnp.manager.webapp.pages.components.species.SpeciesFormPage;

/**
 * Page object for the species detail page
 */
public class SpeciesDetailPage extends DetailPageBase {

    public SpeciesDetailPage(Page page) {
        super(page);
    }

    /*+
     * Returns the locator of the link to a nation detail.
     */
    public Locator getLink(Nation nation) {
        return page.getByTestId(nation.getId().toHexString());
    }

    /**
     * Opens the species creation form.
     */
    public SpeciesFormPage edit() {
        page.getByTestId("edit").click();
        return new SpeciesFormPage(page);
    }
}
