package de.pnp.manager.webapp.pages.components.users;

import com.microsoft.playwright.Locator;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.webapp.pages.components.Select;
import de.pnp.manager.webapp.pages.components.Switch;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
     * @see PnPUser#displayName()
     */
    public void setDisplayName(String name) {
        getByDataPath("displayName").fill(name);
    }

    /**
     * @see PnPUser#email()
     */
    public void setEmail(String email) {
        getByDataPath("email").fill(email);
    }

    /**
     * @see PnPUserDetails#getAuthorities()
     */
    public void setIsAdmin(boolean admin) {
        Switch.from(locator.getByTestId("adminRights")).set(admin);
    }


    /**
     * @see PnPUserDetails#getAuthorities()
     */
    public void setIsUniverseCreator(boolean creator) {
        Switch.from(locator.getByTestId("universeCreationRights")).set(creator);
    }

    /**
     * Checks of the universe creator checkbox is disabled and checked.
     */
    public void assertUniverseCreatorIsCheckedAndDisabled() {
        Switch creationRights = Switch.from(locator.getByTestId("universeCreationRights"));
        assertThat(creationRights.asLocator()).isDisabled();
        assertThat(creationRights.asLocator()).isChecked();
    }

    /**
     * @see PnPUserDetails#getAuthorities()
     */
    public void addReadUniverse(Universe universe) {
        Select.from(locator.getByTestId("universe-read-rights")).select(universe.getId());
    }

    /**
     * @see PnPUserDetails#getAuthorities()
     */
    public void addWriteUniverse(Universe universe) {
        Select.from(locator.getByTestId("universe-write-rights")).select(universe.getId());
    }

    /**
     * @see PnPUserDetails#getAuthorities()
     */
    public void addOwnerUniverse(Universe universe) {
        Select.from(locator.getByTestId("universe-owner-rights")).select(universe.getId());
    }

    /**
     * Returns the locator based on the data path.
     * Ensure that the locator is visible and enabled.
     */
    protected Locator getByDataPath(String path) {
        Locator input = locator.locator("[data-path=\"" + path + "\"]");
        assertThat(input).isVisible();
        assertThat(input).isEnabled();
        return input;
    }
}
