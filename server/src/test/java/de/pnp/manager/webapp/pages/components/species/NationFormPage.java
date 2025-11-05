package de.pnp.manager.webapp.pages.components.species;

import com.microsoft.playwright.Page;
import de.pnp.manager.component.character.Nation;
import de.pnp.manager.webapp.pages.PageBase;
import de.pnp.manager.webapp.pages.components.DatabaseObjectForm;

/**
 * The form for nations.
 */
public class NationFormPage extends PageBase {

    public NationFormPage(Page page) {
        super(page);
    }

    /**
     * Fills out the form.
     */
    public void fillOut(Nation nation) {
        DatabaseObjectForm.fromLocator(page.getByTestId("nation-form")).fillOut(nation);
    }

    /**
     * Tries to add the species to the universe
     */
    public void add() {
        page.locator("[type=submit]").click();
    }

    /**
     * Tries to edit the species to the universe
     */
    public void edit() {
        page.locator("[type=submit]").click();
    }
}
