package de.pnp.manager.webapp.pages.species;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import de.pnp.manager.component.character.Nation;
import de.pnp.manager.component.character.Species;
import de.pnp.manager.webapp.pages.PageBase;
import de.pnp.manager.webapp.pages.components.species.SpeciesFormPage;

/**
 * Page object for the species overview page
 */
public class SpeciesOverviewPage extends PageBase {

    public SpeciesOverviewPage(Page page) {
        super(page);
    }

    /*+
     * Returns the locator of the link to the species detail.
     */
    public Locator getLink(Species species) {
        return page.getByTestId(species.getId().toHexString());
    }

    /*+
     * Returns the locator of the link to the nation detail.
     */
    public Locator getLink(Species species, Nation nation) {
        return page.getByTestId(species.getId().toHexString() + "/" + nation.getId().toHexString());
    }

    /*+
     * Returns the locator of the link to the unbound nation detail.
     */
    public Locator getLink(Nation nation) {
        return page.getByTestId(nation.getId().toHexString());
    }

    /**
     * Opens the {@link SpeciesDetailPage} for the given species.
     */
    public SpeciesDetailPage openSpeciesPage(Species species) {
        getLink(species).click();
        return new SpeciesDetailPage(page);
    }

    /**
     * Opens the {@link NationDetailPage} for the given species.
     */
    public NationDetailPage openNationPage(Species species, Nation nation) {
        getLink(species, nation).click();
        return new NationDetailPage(page);
    }

    /**
     * Opens the species creation form.
     */
    public SpeciesFormPage openSpeciesFormPage() {
        page.getByTestId("add").click();
        return new SpeciesFormPage(page);
    }
}
