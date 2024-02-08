package de.pnp.manager.webapp.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;

/**
 * Util class to web app testing
 */
public abstract class WebTestUtils {

    /**
     * Selects the given option in the given auto complete.
     *
     * @param autocomplete the locator of the autocomplete
     * @param option       the option which should be selected
     * @param exact        if only the exact option should be selected or the first matching one
     */
    public static void selectAutoComplete(Locator autocomplete, String option, boolean exact) {
        autocomplete.click();
        autocomplete.type(option);
        if (exact) {
            autocomplete.page().getByRole(AriaRole.OPTION, new GetByRoleOptions().setName(option).setExact(true))
                .click();
        } else {
            autocomplete.page().getByRole(AriaRole.OPTION, new GetByRoleOptions().setName(option)).first().click();
        }
    }

    /**
     * Selects the given value in a combobox.
     */
    public static void select(Locator select, String value) {
        select.click();
        select.page().getByRole(AriaRole.OPTION).and(select.page().locator("[data-value=\"" + value + "\"]")).click();
    }
}
