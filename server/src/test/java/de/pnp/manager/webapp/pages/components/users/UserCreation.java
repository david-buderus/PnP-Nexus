package de.pnp.manager.webapp.pages.components.users;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserCreation;

/**
 * Represents the user creation dialog.
 */
public class UserCreation extends UserManipulation {

    private UserCreation(Locator locator) {
        super(locator);
    }

    /**
     * Creates the UserCreation from the given {@link Page}.
     */
    public static UserCreation getUserCreation(Page page) {
        return new UserCreation(page.getByTestId("user-creation-dialog"));
    }

    /**
     * @see PnPUser#getUsername()
     */
    public void setUsername(String name) {
        locator.getByTestId("username").getByRole(AriaRole.TEXTBOX).fill(name);
    }

    /**
     * @see PnPUserCreation#getPassword()
     */
    public void setPassword(String password) {
        locator.getByTestId("password").getByRole(AriaRole.TEXTBOX).fill(password);
    }

    /**
     * Tries to add the user.
     */
    public void addUser() {
        locator.getByTestId("user-create").click();
    }
}
