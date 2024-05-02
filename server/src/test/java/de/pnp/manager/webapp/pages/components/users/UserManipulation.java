package de.pnp.manager.webapp.pages.components.users;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.webapp.utils.WebTestUtils;

/**
 * Base class for user manipulation dialogs.
 */
public class UserManipulation {

    /**
     * The locator of the component
     */
    protected final Locator locator;

    protected UserManipulation(Locator locator) {
        this.locator = locator;
    }

    /**
     * @see PnPUser#getDisplayName()
     */
    public void setDisplayName(String name) {
        locator.getByTestId("displayName").getByRole(AriaRole.TEXTBOX).fill(name);
    }

    /**
     * @see PnPUser#getEmail()
     */
    public void setEmail(String email) {
        locator.getByTestId("email").getByRole(AriaRole.TEXTBOX).fill(email);
    }

    /**
     * @see PnPUserDetails#getAuthorities()
     */
    public void setIsAdmin(boolean admin) {
        Locator checkBox = locator.getByTestId("adminRights").getByRole(AriaRole.CHECKBOX);
        if (admin) {
            checkBox.check();
        } else {
            checkBox.uncheck();
        }
    }


    /**
     * @see PnPUserDetails#getAuthorities()
     */
    public void setIsUniverseCreator(boolean creator) {
        Locator checkBox = locator.getByTestId("universeCreationRights").getByRole(AriaRole.CHECKBOX);
        if (creator) {
            checkBox.check();
        } else {
            checkBox.uncheck();
        }
    }

    /**
     * Checks of the universe creator checkbox is disabled and checked.
     */
    public void assertUniverseCreatorIsCheckedAndDisabled() {
        Locator checkBox = locator.getByTestId("universeCreationRights").getByRole(AriaRole.CHECKBOX);
        assertThat(checkBox).isDisabled();
        assertThat(checkBox).isChecked();
    }

    /**
     * @see PnPUserDetails#getAuthorities()
     */
    public void addReadUniverse(String universe) {
        WebTestUtils.selectAutoComplete(locator.getByTestId("universe-read-rights"), universe, true);
    }

    /**
     * @see PnPUserDetails#getAuthorities()
     */
    public void addWriteUniverse(String universe) {
        WebTestUtils.selectAutoComplete(locator.getByTestId("universe-write-rights"), universe, true);
    }

    /**
     * @see PnPUserDetails#getAuthorities()
     */
    public void addOwnerUniverse(String universe) {
        WebTestUtils.selectAutoComplete(locator.getByTestId("universe-owner-rights"), universe, true);
    }
}
