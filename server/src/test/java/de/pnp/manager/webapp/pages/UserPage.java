package de.pnp.manager.webapp.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import de.pnp.manager.webapp.pages.components.users.ChangePassword;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Represents the user page
 */
public class UserPage extends PageBase {

    public UserPage(Page page) {
        super(page);
    }

    /**
     * Asserts the username
     */
    public void assertUsername(String expectedUsername) {
        assertThat(page.getByTestId("username")).hasValue(expectedUsername);
    }

    /**
     * Asserts the displayname
     */
    public void assertDisplayName(String expectedDisplayName) {
        assertThat(getByDataPath("displayName")).hasValue(expectedDisplayName);
    }

    /**
     * Sets the display name
     */
    public void setDisplayName(String displayName) {
        getByDataPath("displayName").fill(displayName);
    }

    /**
     * Asserts the email
     */
    public void assertEmail(String expectedEmail) {
        assertThat(getByDataPath("email")).hasValue(expectedEmail);
    }

    /**
     * Sets the email
     */
    public void setEmail(String email) {
        getByDataPath("email").fill(email);
    }

    /**
     * Returns the error text of the email input field if it exists.
     */
    public void assertEmailError() {
        assertThat(getByDataPath("email")
                .locator("..")
                .locator("..")
                .locator("p")).not().isEmpty();
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

    /**
     * Opens the change password menu.
     */
    public ChangePassword changePassword() {
        page.getByTestId("change-password").click();
        return new ChangePassword(page.getByTestId("change-password-dialog"));
    }

    private Locator getByDataPath(String path) {
        Locator input = page.locator("[data-path=\"" + path + "\"]");
        assertThat(input).isVisible();
        assertThat(input).isEnabled();
        return input;
    }
}
