package de.pnp.manager.webapp.users;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.component.user.PnPUserPreference;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.UiTestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.UserPreferenceRepository;
import de.pnp.manager.webapp.pages.UserPreferencePage;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the user page.
 */
@TestServer(EServerTestConfiguration.EMPTY)
@UiTestServer
public class UserPreferencePageTest extends ServerTestBase {

    private final static String USERNAME = "admin";

    /**
     * The test universe.
     */
    protected Universe universe;

    private UserPreferencePage page;

    @Autowired
    private UserPreferenceRepository preferenceRepository;

    @BeforeEach
    void openItemPage() {
        universe = getUniverse();
        if (preferenceRepository.getPreference(USERNAME).isEmpty()) {
            userController.createNewUser(new PnPUserCreation(USERNAME, USERNAME, USERNAME, "", List.of()));
        }

        page = webDriver.openMainMenu(USERNAME, "admin").openUserPreferencesPage();
    }

    @Test
    void testContent() {
        assertThat(page.getUsername()).isEqualTo(USERNAME);
        page.assertNoLanguageIsSelected();
    }

    @Test
    void testEdit() {
        page.editUser();
        page.setLanguage("en");
        page.saveEditedUser();
        page.assertIsInNonEditMode();

        assertThat(page.getLanguage().trim()).isEqualTo("English");

        PnPUserPreference preference = preferenceRepository.getPreference(USERNAME).orElseThrow();
        assertThat(preference.language()).isEqualTo("en");
    }

    @Test
    void testCancel() {
        page.editUser();
        page.setLanguage("en");
        page.cancelEditUser();

        page.assertIsInNonEditMode();

        page.assertNoLanguageIsSelected();

        PnPUserPreference preference = preferenceRepository.getPreference(USERNAME).orElseThrow();
        assertThat(preference.language()).isBlank();
    }
}
