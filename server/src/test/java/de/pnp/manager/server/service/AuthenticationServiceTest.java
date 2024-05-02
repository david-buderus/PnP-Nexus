package de.pnp.manager.server.service;

import static de.pnp.manager.server.service.ServiceTestUtils.assertHttpStatusException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.contoller.UserController;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.service.UserService.PasswordChange;
import java.util.List;
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
 * Tests for {@link AuthenticationService}.
 */
@AutoConfigureMockMvc
@TestServer(EServerTestConfiguration.EMPTY)
public class AuthenticationServiceTest extends ServerTestBase {

    private static final String BASE_PATH = "/api/authentication";
    private static final String USER = "test-user";

    private static final String USER_PASSWORD = "@@2QTt.ujzKmFVu*";

    private static final PnPUserCreation USER_CREATION = new PnPUserCreation(USER, USER_PASSWORD, USER,
        "user@test.com", List.of());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Test
    @WithMockUser(value = USER)
    void testUpdatePassword() throws Exception {
        userController.createNewUser(USER_CREATION);

        assertHttpStatusException(
            () -> updatePassword(new PasswordChange("FALSE_PASSWORD", USER_PASSWORD + "CHANGE")),
            HttpStatus.BAD_REQUEST);

        String oldPassword = userDetailsRepository.loadUserByUsername(USER).getPassword();
        updatePassword(new PasswordChange(USER_PASSWORD, USER_PASSWORD + "CHANGE"));
        assertThat(userDetailsRepository.loadUserByUsername(USER).getPassword()).isNotEqualTo(oldPassword);
    }

    private void updatePassword(PasswordChange passwordChange) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                post(BASE_PATH + "/password").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(passwordChange)))
            .andReturn().getResponse();

        if (HttpStatus.valueOf(response.getStatus()).isError()) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
