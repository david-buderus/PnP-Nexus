package de.pnp.manager.webapp.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Represents the user page
 */
public class UserPage extends PageBase {

    public UserPage(Page page) {
        super(page);
    }

    /**
     * Returns the username
     */
    public String getUsername() {
        return page.getByTestId("username").getByRole(AriaRole.TEXTBOX).inputValue();
    }

    /**
     * Returns the displayname
     */
    public String getDisplayName() {
        return page.getByTestId("displayname").getByRole(AriaRole.TEXTBOX).inputValue();
    }

    /**
     * Sets the display name
     */
    public void setDisplayName(String displayName) {
        page.getByTestId("displayname").getByRole(AriaRole.TEXTBOX).fill(displayName);
    }

    /**
     * Returns the Email
     */
    public String getEmail() {
        return page.getByTestId("email").getByRole(AriaRole.TEXTBOX).inputValue();
    }

    /**
     * Sets the email
     */
    public void setEmail(String email) {
        page.getByTestId("email").getByRole(AriaRole.TEXTBOX).fill(email);
    }

    /**
     * Returns the error text of the email input field if it exists.
     */
    public String getEmailError() {
        return page.getByTestId("email").locator("[id=email-helper-text]").textContent();
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
