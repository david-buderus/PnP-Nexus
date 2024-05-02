package de.pnp.manager.webapp.pages.components.users;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

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
        return new UserEdit(page.getByTestId("user-edit-dialog"));
    }

    /**
     * Tries to edit the user.
     */
    public void editUser() {
        locator.getByTestId("user-edit").click();
    }
}
