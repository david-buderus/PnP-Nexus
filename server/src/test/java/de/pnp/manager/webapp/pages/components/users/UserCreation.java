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
        return new UserCreation(page.getByRole(AriaRole.DIALOG));
    }

    /**
     * @see PnPUser#username()
     */
    public void setUsername(String name) {
        getByDataPath("username").fill(name);
    }

    /**
     * @see PnPUserCreation#getPassword()
     */
    public void setPassword(String password) {
        getByDataPath("password").fill(password);
    }

    /**
     * Tries to add the user.
     */
    public void addUser() {
        locator.locator("[type=submit]").click();
    }
}
