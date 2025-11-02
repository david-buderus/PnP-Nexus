package de.pnp.manager.webapp.users;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

        page = webDriver.openMainMenu(USERNAME, USERNAME).openUserPage();
    }

    @Test
    void testContent() {
        page.assertUsername(USERNAME);
        page.assertDisplayName(USERNAME);
        page.assertEmail("");
    }

    @Test
    void testEdit() {
        page.editUser();
        page.setDisplayName(NEW_DISPLAYNAME);
        page.setEmail("no.email");
        page.saveEditedUser();

        page.assertIsInEditMode();
        page.assertEmailError();
        page.setEmail(NEW_EMAIL);
        page.saveEditedUser();

        page.assertIsInNonEditMode();

        page.assertDisplayName(NEW_DISPLAYNAME);
        page.assertEmail(NEW_EMAIL);

        PnPUser user = userRepository.getUser(USERNAME).orElseThrow();
        assertThat(user.displayName()).isEqualTo(NEW_DISPLAYNAME);
        assertThat(user.email()).isEqualTo(NEW_EMAIL);
    }

    @Test
    void testCancel() {
        page.editUser();
        page.setDisplayName(NEW_DISPLAYNAME);
        page.setEmail(NEW_EMAIL);
        page.cancelEditUser();

        page.assertIsInNonEditMode();

        page.assertDisplayName(USERNAME);
        page.assertEmail("");

        PnPUser user = userRepository.getUser(USERNAME).orElseThrow();
        assertThat(user.displayName()).isEqualTo(USERNAME);
        assertThat(user.email()).isBlank();
    }

    @Test
    void testChangePassword() throws InterruptedException {
        ChangePassword dialog = page.changePassword();
        dialog.setCurrentPassword("admin");
        dialog.setNewPassword(NEW_PASSWORD);

        dialog.assertThatSaveIsDisabled();

        dialog.setConfirmPassword(NEW_PASSWORD);
        dialog.savePassword();

        dialog.assertIsClosed();

        // We need to wait for the backend to process the change
        Thread.sleep(100);
        assertThat(userDetailsRepository.isValidPassword(USERNAME, NEW_PASSWORD)).isTrue();
    }
}
