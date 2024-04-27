package de.pnp.manager.webapp.pages.components;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions.IsVisibleOptions;
import com.microsoft.playwright.options.AriaRole;

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
        locator.getByTestId("oldPassword").getByRole(AriaRole.TEXTBOX).fill(password);
    }

    /**
     * Sets the new password field
     */
    public void setNewPassword(String password) {
        locator.getByTestId("newPassword").getByRole(AriaRole.TEXTBOX).fill(password);
    }

    /**
     * Sets the confirmation password field
     */
    public void setConfirmPassword(String password) {
        locator.getByTestId("confirmPassword").getByRole(AriaRole.TEXTBOX).fill(password);
    }

    /**
     * Tries to save the password.
     */
    public void savePassword() {
        locator.getByTestId("save-password").click();
    }

    /**
     * Asserts that the save button can not be clicked.
     */
    public void assertThatSaveIsDisabled() {
        assertThat(locator.getByTestId("save-password")).isDisabled();
    }

    /**
     * Asserts that the dialog has been closed.
     */
    public void assertIsClosed() {
        assertThat(locator).isVisible(new IsVisibleOptions().setVisible(false));
    }
}
