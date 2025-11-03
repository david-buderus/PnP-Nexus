package de.pnp.manager.webapp.pages.components.users;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Represents the user edit dialog.
 */
public class UserEdit extends UserManipulation {

    private UserEdit(Locator locator) {
        super(locator);
    }

    /**
     * Creates the UserCreation from the given {@link Page}.
     */
    public static UserEdit getUserEdit(Page page) {
        return new UserEdit(page.getByRole(AriaRole.DIALOG));
    }

    /**
     * Tries to edit the user.
     */
    public void editUser() {
        locator.locator("[type=submit]").click();
    }
}
