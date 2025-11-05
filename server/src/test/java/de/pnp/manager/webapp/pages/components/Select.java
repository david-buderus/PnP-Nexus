package de.pnp.manager.webapp.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import de.pnp.manager.component.DatabaseObject;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * A select box in the webapp.
 */
public class Select extends ComponentBase {

    private Select(Locator locator) {
        super(locator);
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

        assertThat(locator).isVisible();
        locator.click(new Locator.ClickOptions().setForce(true));

        Locator option = locator.page().getByRole(AriaRole.OPTION)
                .and(locator.page().locator("[value=\"" + value + "\"]"));
        option.click();

        if (wait) {
            if (isMultiSelect()) {
                assertThat(locator.locator("..")).not().isEmpty();
                // Close the multi selection
                locator.press("Escape");
            } else {
                assertThat(locator).not().isEmpty();
            }
        }
    }


    /**
     * Selects the given value.
     */
    public void select(DatabaseObject object) {
        select(object.getId().toHexString());
    }

    /**
     * Deselects the current value.
     */
    public void deselect() {
        if (isMultiSelect()) {
            locator.locator("..").locator("span >> button").all().forEach(Locator::click);
        } else {
            // Sometimes you need to deselect an items twice due to Mantine
            normalDeselect();
            normalDeselect();
        }
    }

    private void normalDeselect() {
        if (locator.inputValue().isBlank()) {
            return;
        }

        locator.click();

        Page page = locator.page();
        Locator dropdown = page.getByRole(AriaRole.LISTBOX);
        dropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        Locator selectedOption = page.getByRole(AriaRole.OPTION)
                .and(page.locator("[data-checked=\"true\"]"));

        if (selectedOption.count() == 0) {
            page.keyboard().press("Escape");
            return;
        }

        selectedOption.first().click();

        dropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
    }

    private boolean isMultiSelect() {
        return locator.getAttribute("class").contains("MultiSelect");
    }
}
