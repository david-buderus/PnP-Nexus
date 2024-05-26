package de.pnp.manager.webapp.users;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.server.ManipulatesMetadata;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.UiTestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.UserRepository;
import de.pnp.manager.webapp.pages.UserPage;
import de.pnp.manager.webapp.pages.components.users.ChangePassword;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the user page.
 */
@TestServer(EServerTestConfiguration.EMPTY)
@UiTestServer
@ManipulatesMetadata
public class UserPageTest extends ServerTestBase {

    private final static String USERNAME = "admin";
    private final static String NEW_DISPLAYNAME = "Administrator";
    private final static String NEW_EMAIL = "admin@example.com";
    private final static String NEW_PASSWORD = "gLQ@oWEbtJi9E6Wx";

    private UserPage page;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @BeforeEach
    void openItemPage() {
        if (userRepository.getUser(USERNAME).isEmpty()) {
            userController.createNewUser(new PnPUserCreation(USERNAME, USERNAME, USERNAME, "", List.of()));
        }

        page = webDriver.openMainMenu(USERNAME, "admin").openUserPage();
    }

    @Test
    void testContent() {
        assertThat(page.getUsername()).isEqualTo(USERNAME);
        assertThat(page.getDisplayName()).isEqualTo(USERNAME);
        assertThat(page.getEmail()).isEmpty();
    }

    @Test
    void testEdit() {
        page.editUser();
        page.setDisplayName(NEW_DISPLAYNAME);
        page.setEmail("no.email");
        page.saveEditedUser();

        page.assertIsInEditMode();
        assertThat(page.getEmailError()).isNotBlank();
        page.setEmail(NEW_EMAIL);
        page.saveEditedUser();

        page.assertIsInNonEditMode();

        assertThat(page.getDisplayName()).isEqualTo(NEW_DISPLAYNAME);
        assertThat(page.getEmail()).isEqualTo(NEW_EMAIL);

        PnPUser user = userRepository.getUser(USERNAME).orElseThrow();
        assertThat(user.getDisplayName()).isEqualTo(NEW_DISPLAYNAME);
        assertThat(user.getEmail()).isEqualTo(NEW_EMAIL);
    }

    @Test
    void testCancel() {
        page.editUser();
        page.setDisplayName(NEW_DISPLAYNAME);
        page.setEmail(NEW_EMAIL);
        page.cancelEditUser();

        page.assertIsInNonEditMode();

        assertThat(page.getDisplayName()).isEqualTo(USERNAME);
        assertThat(page.getEmail()).isBlank();

        PnPUser user = userRepository.getUser(USERNAME).orElseThrow();
        assertThat(user.getDisplayName()).isEqualTo(USERNAME);
        assertThat(user.getEmail()).isBlank();
    }

    @Test
    void testChangePassword() {
        ChangePassword dialog = page.changePassword();
        dialog.setCurrentPassword("admin");
        dialog.setNewPassword(NEW_PASSWORD);

        dialog.assertThatSaveIsDisabled();

        dialog.setConfirmPassword(NEW_PASSWORD);
        dialog.savePassword();

        dialog.assertIsClosed();
        assertThat(userDetailsRepository.isValidPassword(USERNAME, NEW_PASSWORD)).isTrue();
    }
}
