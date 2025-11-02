package de.pnp.manager.webapp.pages;

import com.microsoft.playwright.Page;
import de.pnp.manager.webapp.pages.components.Select;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static de.pnp.manager.webapp.utils.WebTestUtils.getByDataPath;

/**
 * Represents the user page
 */
public class UserPreferencePage extends PageBase {

    public UserPreferencePage(Page page) {
        super(page);
    }

    /**
     * Returns the selected language
     */
    public void assertLanguage(String expectedLanguage) {
        assertThat(getByDataPath(page, "language")).hasValue(expectedLanguage);
    }

    /**
     * Sets the language
     */
    public void setLanguage(String language) {
        Select.from(getByDataPath(page, "language")).select(language);
    }

    /**
     * Asserts that no language is selected
     */
    public void assertNoLanguageIsSelected() {
        assertThat(getByDataPath(page, "language")).isEmpty();
    }

    /**
     * Switches to edit mode.
     */
    public void editUser() {
        page.getByTestId("edit").click();
    }

    /**
     * Cancels to edit mode.
     */
    public void cancelEditUser() {
        page.getByTestId("cancel").click();
    }


    /**
     * Saves the edited user.
     */
    public void saveEditedUser() {
        page.getByTestId("save").click();
    }

    /**
     * Asserts that the page is in non-edit mode.
     */
    public void assertIsInNonEditMode() {
        assertThat(page.getByTestId("edit")).isVisible();
    }
}
