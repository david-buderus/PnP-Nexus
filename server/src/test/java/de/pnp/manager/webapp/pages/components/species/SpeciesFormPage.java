package de.pnp.manager.webapp.pages.components.species;

import com.microsoft.playwright.Page;
import de.pnp.manager.component.character.Species;
import de.pnp.manager.webapp.pages.PageBase;
import de.pnp.manager.webapp.pages.components.DatabaseObjectForm;

/**
 * The form for species.
 */
public class SpeciesFormPage extends PageBase {

    public SpeciesFormPage(Page page) {
        super(page);
    }

    /**
     * Fills out the form.
     */
    public void fillOut(Species species) {
        DatabaseObjectForm.fromLocator(page.getByTestId("species-form")).fillOut(species);
    }

    /**
     * Tries to add the species to the universe
     */
    public void add() {
        page.locator("[type=submit]").click();
    }
}
