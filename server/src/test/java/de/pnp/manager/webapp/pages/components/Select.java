package de.pnp.manager.webapp.pages.components;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;

/**
 * A select box in the webapp.
 */
public class Select {

    private final Locator locator;

    private Select(Locator locator) {
        this.locator = locator;
    }

    /**
     * Returns a select defined by the given locator
     */
    public static Select from(Locator locator) {
        return new Select(locator);
    }


    /**
     * Selects the given value.
     */
    public void select(String value) {
        select(value, true);
    }

    /**
     * Selects the given value.
     */
    public void select(String value, boolean wait) {
        deselect();

        locator.click();
        Locator option = locator.page().getByRole(AriaRole.OPTION)
            .and(locator.page().locator("[value=\"" + value + "\"]"));
        option.click();

        if (wait) {
            assertThat(locator).not().isEmpty();
        }
    }


    /**
     * Selects the given value.
     */
    public void select(DatabaseObject object) {
        select(object.getId().toHexString());
    }


    /**
     * Selects the given value.
     */
    public void select(IUniquelyNamedDataObject object) {
        select(object.getId().toHexString());
        assertThat(locator).hasValue(object.getName());
    }

    /**
     * Deselects the current value.
     */
    public void deselect() {
        if (locator.inputValue().isBlank()) {
            return;
        }

        locator.click();
        locator.page().getByRole(AriaRole.OPTION)
            .and(locator.page().locator("[data-checked=\"true\"]")).click();
    }
}
