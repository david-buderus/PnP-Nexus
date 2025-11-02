package de.pnp.manager.webapp.pages.components.users;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions.IsVisibleOptions;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Represents the change password dialog.
 */
public class ChangePassword {

    /**
     * The locator of the component
     */
    private final Locator locator;

    public ChangePassword(Locator locator) {
        this.locator = locator;
    }

    /**
     * Sets the current password field
     */
    public void setCurrentPassword(String password) {
        getByDataPath("oldPassword").fill(password);
    }

    /**
     * Sets the new password field
     */
    public void setNewPassword(String password) {
        getByDataPath("newPassword").fill(password);
    }

    /**
     * Sets the confirmation password field
     */
    public void setConfirmPassword(String password) {
        locator.getByTestId("confirmPassword").fill(password);
    }

    /**
     * Tries to save the password.
     */
    public void savePassword() {
        locator.locator("[type=submit]").click();
    }

    /**
     * Asserts that the save button can not be clicked.
     */
    public void assertThatSaveIsDisabled() {
        assertThat(locator.locator("[type=submit]")).isDisabled();
    }

    /**
     * Asserts that the dialog has been closed.
     */
    public void assertIsClosed() {
        assertThat(locator).isVisible(new IsVisibleOptions().setVisible(false));
    }

    private Locator getByDataPath(String path) {
        Locator input = locator.locator("[data-path=\"" + path + "\"]");
        assertThat(input).isVisible();
        assertThat(input).isEnabled();
        return input;
    }
}
