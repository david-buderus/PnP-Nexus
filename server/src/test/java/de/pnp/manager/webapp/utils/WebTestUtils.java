package de.pnp.manager.webapp.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;

/**
 * Util class to web app testing
 */
public abstract class WebTestUtils {

    /**
     * Selects the given option in the given auto complete.
     *
     * @param select the locator of the autocomplete
     * @param option the option which should be selected
     */
    public static void selectWithSearch(Locator select, String searchText, String option) {
        select.click();
        select.type(searchText);
        select.page().getByRole(AriaRole.OPTION).and(select.page().locator("[value=\"" + option + "\"]"))
            .click();
    }

    /**
     * Clears the given auto complete.
     */
    public static void clearMultiSelect(Locator autocomplete) {
        autocomplete.getByRole(AriaRole.BUTTON).all().forEach(Locator::click);
    }
}
