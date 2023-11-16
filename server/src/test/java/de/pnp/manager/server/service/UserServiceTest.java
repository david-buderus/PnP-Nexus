package de.pnp.manager.server.service;

import static de.pnp.manager.server.service.ServiceTestUtils.assertForbidden;
import static de.pnp.manager.server.service.ServiceTestUtils.assertHttpStatusException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import de.pnp.manager.component.user.GrantedUniverseAuthority;
import de.pnp.manager.component.user.IGrantedAuthorityDTO;
import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.security.SecurityConstants;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.contoller.UserController;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.UserRepository;
import de.pnp.manager.server.service.UserService.PasswordChange;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

/**
 * Tests for {@link UserService}.
 */
@AutoConfigureMockMvc
@TestServer(EServerTestConfiguration.EMPTY)
public class UserServiceTest extends ServerTestBase {

    private static final String BASE_PATH = "/api/users";
    private static final String USER = "test-user";
    private static final String OTHER_USER = "other-user";
    private static final String UNIVERSE = "test-universe";
    private static final String EMAIL = "user@test.com";
    private static final PnPUserCreation USER_CREATION = new PnPUserCreation(USER, USER, USER,
        EMAIL, List.of(IGrantedAuthorityDTO.from(GrantedUniverseAuthority.readAuthority(UNIVERSE))));
    private static final PnPUserCreation OTHER_USER_CREATION = new PnPUserCreation(OTHER_USER, OTHER_USER, OTHER_USER,
        EMAIL, List.of(IGrantedAuthorityDTO.from(GrantedUniverseAuthority.writeAuthority(UNIVERSE))));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Nested
    @DisplayName("as admin")
    class AsAdminTest {

        @Test
        @WithMockUser(value = USER, roles = SecurityConstants.ADMIN)
        void testAddUser() throws Exception {
            createUser(OTHER_USER_CREATION);
            Optional<PnPUser> user = userRepository.getUser(OTHER_USER);
            assertThat(user).isPresent();
            assertThat(user.get()).isEqualTo(new PnPUser(OTHER_USER, OTHER_USER, EMAIL));

            Optional<PnPUserDetails> userDetails = userDetailsRepository.getUser(OTHER_USER);
            assertThat(userDetails).isPresent();
            assertThat(userDetails.get().getUsername()).isEqualTo(OTHER_USER);
            assertThat(userDetails.get().getPassword()).doesNotContain("{noop}");
            assertThat(userDetails.get().getPassword()).isNotEqualTo(OTHER_USER);
            assertThat(userDetails.get().getAuthorities()).map(GrantedUniverseAuthority.class::cast)
                .containsExactly(GrantedUniverseAuthority.writeAuthority(UNIVERSE));
        }

        @Test
        @WithMockUser(value = USER, roles = SecurityConstants.ADMIN)
        void testGetUser() throws Exception {
            userController.createNewUser(OTHER_USER_CREATION);
            assertThat(getOne(OTHER_USER)).isEqualTo(new PnPUser(OTHER_USER, OTHER_USER, EMAIL));
        }

        @Test
        @WithMockUser(value = USER, roles = SecurityConstants.ADMIN)
        void testUpdateUser() throws Exception {
            userController.createNewUser(OTHER_USER_CREATION);

            String otherEmail = "other@test.com";
            updateUser(OTHER_USER, new PnPUser(OTHER_USER, OTHER_USER, otherEmail));
            assertThat(getOne(OTHER_USER)).isEqualTo(new PnPUser(OTHER_USER, OTHER_USER, otherEmail));
        }

        @Test
        @WithMockUser(value = USER, roles = SecurityConstants.ADMIN)
        void testDeleteUser() throws Exception {
            userController.createNewUser(OTHER_USER_CREATION);

            deleteOne(OTHER_USER);
            assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(() -> getOne(OTHER_USER))
                .extracting(ResponseStatusException::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @WithMockUser(value = USER, roles = SecurityConstants.ADMIN)
        void testUpdatePermissions() throws Exception {
            userController.createNewUser(OTHER_USER_CREATION);
            updatePermissions(OTHER_USER, List.of());
            assertThat(getPermissions(OTHER_USER)).isEmpty();
        }

        @Test
        @WithMockUser(value = USER, roles = SecurityConstants.ADMIN)
        void testGetPermissions() throws Exception {
            userController.createNewUser(OTHER_USER_CREATION);
            assertThat(getPermissions(OTHER_USER)).containsExactlyInAnyOrderElementsOf(
                OTHER_USER_CREATION.getAuthorities());
        }

        @Test
        @WithMockUser(value = USER, roles = SecurityConstants.ADMIN)
        void testUpdatePassword() {
            userController.createNewUser(OTHER_USER_CREATION);

            assertForbidden(() -> updatePassword(OTHER_USER, new PasswordChange(OTHER_USER, "OTHER")));
        }
    }

    @Nested
    @DisplayName("as user")
    class AsUserTest {

        @Test
        @WithMockUser(value = USER)
        void testAddUser() {
            assertForbidden(() -> createUser(OTHER_USER_CREATION));
            assertThat(userController.exists(OTHER_USER)).isFalse();
        }

        @Test
        @WithMockUser(value = USER)
        void testGetUser() throws Exception {
            userController.createNewUser(USER_CREATION);
            userController.createNewUser(OTHER_USER_CREATION);
            assertThat(getOne(USER)).isEqualTo(new PnPUser(USER, USER, EMAIL));
            assertForbidden(() -> getOne(OTHER_USER));
        }

        @Test
        @WithMockUser(value = USER)
        void testUpdateUser() throws Exception {
            userController.createNewUser(USER_CREATION);
            userController.createNewUser(OTHER_USER_CREATION);

            String otherEmail = "other@test.com";
            updateUser(USER, new PnPUser(USER, USER, otherEmail));
            assertThat(getOne(USER)).isEqualTo(new PnPUser(USER, USER, otherEmail));

            assertForbidden(() -> updateUser(OTHER_USER, new PnPUser(OTHER_USER, OTHER_USER, otherEmail)));
        }

        @Test
        @WithMockUser(value = USER)
        void testDeleteUser() throws Exception {
            userController.createNewUser(USER_CREATION);
            userController.createNewUser(OTHER_USER_CREATION);

            deleteOne(USER);
            assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(() -> getOne(USER))
                .extracting(ResponseStatusException::getStatusCode).isEqualTo(HttpStatus.NOT_FOUND);

            assertForbidden(() -> deleteOne(OTHER_USER));
        }

        @Test
        @WithMockUser(value = USER)
        void testUpdatePermissions() {
            userController.createNewUser(USER_CREATION);
            userController.createNewUser(OTHER_USER_CREATION);

            assertForbidden(() -> updatePermissions(USER, List.of()));
            assertForbidden(() -> updatePermissions(OTHER_USER, List.of()));
        }

        @Test
        @WithMockUser(value = USER)
        void testGetPermissions() throws Exception {
            userController.createNewUser(USER_CREATION);
            userController.createNewUser(OTHER_USER_CREATION);

            assertThat(getPermissions(USER)).containsExactlyInAnyOrderElementsOf(
                USER_CREATION.getAuthorities());
            assertForbidden(() -> getPermissions(OTHER_USER));
        }

        @Test
        @WithMockUser(value = USER)
        void testUpdatePassword() throws Exception {
            userController.createNewUser(USER_CREATION);
            userController.createNewUser(OTHER_USER_CREATION);

            assertHttpStatusException(() -> updatePassword(USER, new PasswordChange("FALSE_PASSWORD", "OTHER")),
                HttpStatus.UNAUTHORIZED);

            String oldPassword = userDetailsRepository.loadUserByUsername(USER).getPassword();
            updatePassword(USER, new PasswordChange(USER, "OTHER"));
            assertThat(userDetailsRepository.loadUserByUsername(USER).getPassword()).isNotEqualTo(oldPassword);

            assertForbidden(() -> updatePassword(OTHER_USER, new PasswordChange(OTHER_USER, "OTHER")));
        }
    }

    private void createUser(PnPUserCreation userCreation) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                post(BASE_PATH).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userCreation)))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private PnPUser getOne(String username) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(BASE_PATH + "/{username}", username))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readValue(response.getContentAsString(), PnPUser.class);
    }

    private void updateUser(String username, PnPUser user) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                put(BASE_PATH + "/{user}", username).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void deleteOne(String username) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete(BASE_PATH + "/{username}", username).with(csrf()))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void updatePermissions(String username, Collection<IGrantedAuthorityDTO> authorities) throws Exception {
        CollectionType collectionType = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, IGrantedAuthorityDTO.class);

        MockHttpServletResponse response = mockMvc.perform(
                post(BASE_PATH + "/{username}/permissions", username).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writerFor(collectionType).writeValueAsString(authorities)))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Collection<IGrantedAuthorityDTO> getPermissions(String username) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(BASE_PATH + "/{username}/permissions", username))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readerForListOf(IGrantedAuthorityDTO.class).readValue(response.getContentAsString());
    }

    private void updatePassword(String username, PasswordChange passwordChange) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                post(BASE_PATH + "/{user}/password", username).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(passwordChange)))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
