package de.pnp.manager.webapp.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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

    /**
     * Returns the locator given the path.
     */
    public static Locator getByDataPath(Locator locator, String path) {
        Locator input = locator.locator("[data-path=\"" + path + "\"]");
        assertThat(input).isVisible();
        assertThat(input).isEnabled();
        return input;
    }

    /**
     * Returns the locator given the path.
     */
    public static Locator getByDataPath(Page page, String path) {
        Locator input = page.locator("[data-path=\"" + path + "\"]");
        assertThat(input).isVisible();
        assertThat(input).isEnabled();
        return input;
    }
}
