package de.pnp.manager.webapp.users;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.component.user.GrantedUniverseAuthority;
import de.pnp.manager.component.user.IGrantedAuthorityDTO.RoleAuthorityDTO;
import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.security.SecurityConstants;
import de.pnp.manager.server.ManipulatesMetadata;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.UiTestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.contoller.UserController;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.UserRepository;
import de.pnp.manager.webapp.pages.UserOverviewPage;
import de.pnp.manager.webapp.pages.components.OverviewTable;
import de.pnp.manager.webapp.pages.components.users.UserCreation;
import de.pnp.manager.webapp.pages.components.users.UserEdit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Tests the user overview page.
 */
@TestServer(EServerTestConfiguration.EMPTY)
@UiTestServer
@ManipulatesMetadata
public class UserOverviewPageTest extends ServerTestBase {

    private final static String ADMIN_USERNAME = "admin";

    private final static String USER_USERNAME = "overview-test-user";
    private final static String USER_DISPLAYNAME = "Example User";
    private final static String USER_EMAIL = "user@example.com";
    private final static String USER_PASSWORD = "gLQ@oWEbtJi9E6Wx";

    private final static String NEW_DISPLAYNAME = "Other Example User";
    private final static String NEW_EMAIL = "other@example.com";


    /**
     * The test universe.
     */
    protected Universe universe;

    private UserOverviewPage page;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserController userController;

    @BeforeEach
    void openUserOverviewPage() {
        universe = createUniverse("users-test", "Test Universe");
        if (userRepository.getUser(ADMIN_USERNAME).isEmpty()) {
            userController.createNewUser(
                new PnPUserCreation(ADMIN_USERNAME, ADMIN_USERNAME, ADMIN_USERNAME, "",
                    List.of(new RoleAuthorityDTO(SecurityConstants.ADMIN))));
        }

        page = webDriver.openMainMenu(ADMIN_USERNAME, "admin").openUserOverviewPage();
    }

    @Test
    void testContainsAdminUser() {
        OverviewTable table = page.getTable();
        table.assertThatTableRowExists(ADMIN_USERNAME);
    }

    @Test
    void testAddUser() {
        UserCreation userCreation = page.openUserAddMenu();
        userCreation.setUsername(USER_USERNAME);
        userCreation.setDisplayName(USER_DISPLAYNAME);
        userCreation.setEmail(USER_EMAIL);
        userCreation.setPassword(USER_PASSWORD);

        userCreation.setIsAdmin(true);
        userCreation.assertUniverseCreatorIsCheckedAndDisabled();
        userCreation.setIsAdmin(false);

        userCreation.setIsUniverseCreator(true);
        userCreation.addReadUniverse(universe.getDisplayName());
        userCreation.addUser();

        OverviewTable table = page.getTable();
        table.assertThatTableRowExists(USER_USERNAME);

        Optional<PnPUser> user = userRepository.getUser(USER_USERNAME);
        assertThat(user).isPresent();
        assertThat(user.get()).isEqualTo(new PnPUser(USER_USERNAME, USER_DISPLAYNAME, USER_EMAIL));

        Optional<PnPUserDetails> userDetails = userDetailsRepository.getUser(USER_USERNAME);
        assertThat(userDetails).isPresent();
        assertThat(userDetails.get().getAuthorities()).map(a -> (GrantedAuthority) a)
            .containsExactlyInAnyOrder(GrantedUniverseAuthority.readAuthority(universe.getName()),
                new SimpleGrantedAuthority(SecurityConstants.UNIVERSE_CREATOR_ROLE));
    }

    @Test
    void testDeleteUser() {
        userController.createNewUser(
            new PnPUserCreation(USER_USERNAME, USER_PASSWORD, USER_DISPLAYNAME, USER_EMAIL, List.of()));
        page.asPage().reload();

        OverviewTable table = page.getTable();
        table.assertThatTableRowExists(USER_USERNAME);
        assertThat(page.isDeleteDisabled()).isTrue();

        table.getTableRow(USER_USERNAME).select();
        page.deleteSelectedUsers();
        table.assertExactlyEntries(1);
        table.assertThatTableRowNotExists(USER_USERNAME);

        assertThat(userController.exists(USER_USERNAME)).isFalse();
    }

    @Test
    void testEditButton() {
        userController.createNewUser(
            new PnPUserCreation(USER_USERNAME, USER_PASSWORD, USER_DISPLAYNAME, USER_EMAIL, List.of()));
        page.asPage().reload();

        OverviewTable table = page.getTable();
        table.assertThatTableRowExists(USER_USERNAME);
        assertThat(page.isEditDisabled()).isTrue();

        table.getTableRow(ADMIN_USERNAME).select();
        assertThat(page.isEditDisabled()).isFalse();

        table.getTableRow(USER_USERNAME).select();
        assertThat(page.isEditDisabled()).isTrue();
    }

    @Test
    void testEditUser() {
        userController.createNewUser(
            new PnPUserCreation(USER_USERNAME, USER_PASSWORD, USER_DISPLAYNAME, USER_EMAIL, List.of()));
        page.asPage().reload();

        OverviewTable table = page.getTable();
        table.getTableRow(USER_USERNAME).select();

        UserEdit userEdit = page.openUserEditMenu();
        userEdit.setDisplayName(NEW_DISPLAYNAME);
        userEdit.setEmail(NEW_EMAIL);
        userEdit.addWriteUniverse(universe.getDisplayName());
        userEdit.editUser();

        table.assertThatTableRowExists(USER_USERNAME);

        Optional<PnPUser> user = userRepository.getUser(USER_USERNAME);
        assertThat(user).isPresent();
        assertThat(user.get()).isEqualTo(new PnPUser(USER_USERNAME, NEW_DISPLAYNAME, NEW_EMAIL));

        Optional<PnPUserDetails> userDetails = userDetailsRepository.getUser(USER_USERNAME);
        assertThat(userDetails).isPresent();
        assertThat(userDetails.get().getAuthorities()).map(a -> (GrantedAuthority) a)
            .containsExactlyInAnyOrder(GrantedUniverseAuthority.writeAuthority(universe.getName()));
    }
}
