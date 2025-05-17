package de.pnp.manager.webapp.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.webapp.pages.components.Select;
import org.assertj.core.api.Assertions;

/**
 * Represents the user page
 */
public class UserPreferencePage extends PageBase {

    public UserPreferencePage(Page page) {
        super(page);
    }

    /**
     * Returns the username
     */
    public String getUsername() {
        return page.getByTestId("username").getByRole(AriaRole.TEXTBOX).inputValue();
    }

    /**
     * Returns the selected language
     */
    public String getLanguage() {
        return page.getByTestId("language").getByRole(AriaRole.COMBOBOX).textContent();
    }

    /**
     * Sets the language
     */
    public void setLanguage(String language) {
        Select.from(page.getByTestId("language")).select(language);
    }

    /**
     * Asserts that no language is selected
     */
    public void assertNoLanguageIsSelected() {
        // Remove zero space whitespaces
        Assertions.assertThat(getLanguage().replaceAll("\\p{Cf}", "")).isBlank();
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

    /**
     * Asserts that the page is in edit mode.
     */
    public void assertIsInEditMode() {
        assertThat(page.getByTestId("save")).isVisible();
    }
}
