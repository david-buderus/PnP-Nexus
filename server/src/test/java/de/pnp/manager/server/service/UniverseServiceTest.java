package de.pnp.manager.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.component.Universe;
import de.pnp.manager.component.user.GrantedUniverseAuthority;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.security.SecurityConstants;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.UniverseRepository;
import de.pnp.manager.server.database.UserDetailsRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

/**
 * Tests for {@link UniverseService}.
 */
@AutoConfigureMockMvc
@TestServer(EServerTestConfiguration.EMPTY)
public class UniverseServiceTest extends ServerTestBase {

    private static final String BASE_PATH = "/api/universes";
    private static final String USER = "test-user";
    private static final String UNIVERSE_NAME = "example-universe";
    private static final String OTHER_UNIVERSE_NAME = "other-universe";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDetailsRepository userRepository;

    @Autowired
    private UniverseRepository universeRepository;

    @Nested
    @DisplayName("as admin")
    class AsAdminTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(
                new PnPUserCreation(USER, USER, USER, null, List.of(new SimpleGrantedAuthority(
                    SecurityConstants.ADMIN_ROLE))));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testCreate() throws Exception {
            runCreateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() throws Exception {
            runUpdateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() throws Exception {
            runDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            runGetTest();
        }
    }

    @Nested
    @DisplayName("as creator/owner")
    class AsCreatorTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(
                new PnPUserCreation(USER, USER, USER, null, List.of(new SimpleGrantedAuthority(
                    SecurityConstants.UNIVERSE_CREATOR_ROLE), GrantedUniverseAuthority.ownerAuthority(UNIVERSE_NAME))));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testCreate() throws Exception {
            runCreateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() throws Exception {
            runUpdateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() throws Exception {
            runDeleteTest();
        }

        @Test
        @WithMockUser(value = "test", roles = SecurityConstants.UNIVERSE_CREATOR)
        void testOwnerRole() throws Exception {
            userRepository.addNewUser("test", "test",
                List.of(new SimpleGrantedAuthority(SecurityConstants.UNIVERSE_CREATOR_ROLE)));

            Universe exampleUniverse = new Universe(UNIVERSE_NAME, "Example Universe");
            create(exampleUniverse);
            assertThat(userRepository.loadUserByUsername("test").getAuthorities()).filteredOn(
                    auth -> auth instanceof GrantedUniverseAuthority).hasSize(1)
                .anyMatch(auth -> ((GrantedUniverseAuthority) auth).isOwner(exampleUniverse.getName()));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            runGetOnlyOneTest();
        }
    }

    @Nested
    @DisplayName("as writer")
    class AsWriterTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(
                new PnPUserCreation(USER, USER, USER, null, List.of(GrantedUniverseAuthority.writeAuthority(UNIVERSE_NAME))));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testCreate() throws Exception {
            runNotAllowedCreateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() throws Exception {
            runNotAllowedUpdateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() throws Exception {
            runNotAllowedDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            runGetOnlyOneTest();
        }
    }

    @Nested
    @DisplayName("as reader")
    class AsReaderTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(
                new PnPUserCreation(USER, USER, USER, null, List.of(GrantedUniverseAuthority.readAuthority(UNIVERSE_NAME))));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testCreate() throws Exception {
            runNotAllowedCreateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() throws Exception {
            runNotAllowedUpdateTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() throws Exception {
            runNotAllowedDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            runGetOnlyOneTest();
        }
    }

    private void runCreateTest() throws Exception {
        Universe exampleUniverse = new Universe(UNIVERSE_NAME, "Example Universe");
        Universe persistedExampleUniverse = create(exampleUniverse);
        assertThat(persistedExampleUniverse).isEqualTo(exampleUniverse);
        assertThat(getOne(exampleUniverse.getName())).isEqualTo(exampleUniverse);
        assertThat(getAll()).containsExactly(exampleUniverse);
    }

    private void runNotAllowedCreateTest() throws Exception {
        Universe exampleUniverse = new Universe(UNIVERSE_NAME, "Example Universe");
        assertThatExceptionOfType(ResponseStatusException.class)
            .isThrownBy(() -> create(exampleUniverse))
            .extracting(ResponseStatusException::getStatusCode).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private void runUpdateTest() throws Exception {
        Universe exampleUniverse = new Universe(UNIVERSE_NAME, "Example Universe");
        universeRepository.insert(exampleUniverse);

        assertThat(getOne(exampleUniverse.getName())).isEqualTo(exampleUniverse);

        Universe changedUniverse = new Universe(exampleUniverse.getName(), "Other Title",
            exampleUniverse.getSettings());
        Universe oldUniverse = update(changedUniverse);
        assertThat(oldUniverse).isEqualTo(exampleUniverse);
        assertThat(getOne(exampleUniverse.getName())).isEqualTo(changedUniverse);
        assertThat(getAll()).containsExactly(changedUniverse);
    }

    private void runNotAllowedUpdateTest() throws Exception {
        Universe exampleUniverse = new Universe(UNIVERSE_NAME, "Example Universe");
        universeRepository.insert(exampleUniverse);

        assertThat(getOne(exampleUniverse.getName())).isEqualTo(exampleUniverse);

        assertThatExceptionOfType(ResponseStatusException.class)
            .isThrownBy(() -> update(exampleUniverse))
            .extracting(ResponseStatusException::getStatusCode).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private void runDeleteTest() throws Exception {
        Universe exampleUniverse = new Universe(UNIVERSE_NAME, "Example Universe");
        universeRepository.insert(exampleUniverse);
        assertThat(getOne(exampleUniverse.getName())).isEqualTo(exampleUniverse);

        deleteOne(exampleUniverse.getName());

        assertThat(getAll()).isEmpty();
        assertThatThrownBy(() -> getOne(exampleUniverse.getName())).isInstanceOf(ResponseStatusException.class)
            .extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private void runNotAllowedDeleteTest() throws Exception {
        Universe exampleUniverse = new Universe(UNIVERSE_NAME, "Example Universe");
        universeRepository.insert(exampleUniverse);
        assertThat(getOne(exampleUniverse.getName())).isEqualTo(exampleUniverse);

        assertThatThrownBy(() -> deleteOne(UNIVERSE_NAME)).isInstanceOf(ResponseStatusException.class)
            .extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private void runGetTest() throws Exception {
        Universe exampleUniverse = new Universe(UNIVERSE_NAME, "Example Universe");
        universeRepository.insert(exampleUniverse);
        Universe otherUniverse = new Universe(OTHER_UNIVERSE_NAME, "Other Universe");
        universeRepository.insert(otherUniverse);

        assertThat(getOne(UNIVERSE_NAME)).isEqualTo(exampleUniverse);
        assertThat(getOne(OTHER_UNIVERSE_NAME)).isEqualTo(otherUniverse);
        assertThat(getAll()).containsExactly(exampleUniverse, otherUniverse);
    }

    private void runGetOnlyOneTest() throws Exception {
        Universe exampleUniverse = new Universe(UNIVERSE_NAME, "Example Universe");
        universeRepository.insert(exampleUniverse);
        Universe otherUniverse = new Universe(OTHER_UNIVERSE_NAME, "Other Universe");
        universeRepository.insert(otherUniverse);

        assertThat(getOne(UNIVERSE_NAME)).isEqualTo(exampleUniverse);
        assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(() -> getOne(OTHER_UNIVERSE_NAME))
            .extracting(ResponseStatusException::getStatusCode).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(getAll()).containsExactly(exampleUniverse);

    }

    private List<Universe> getAll() throws Exception {
        String json = mockMvc.perform(get(BASE_PATH)).andExpect(status().isOk()).andReturn()
            .getResponse().getContentAsString();
        return objectMapper.readerForListOf(Universe.class).readValue(json);
    }

    private Universe getOne(String universe) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(BASE_PATH + "/{universe}", universe))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readValue(response.getContentAsString(), Universe.class);
    }

    private Universe create(Universe universe) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                post(BASE_PATH).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(universe)))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readValue(response.getContentAsString(), Universe.class);
    }

    private Universe update(Universe universe) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                put(BASE_PATH + "/{universe}", universe.getName()).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(universe)))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }
        return objectMapper.readValue(response.getContentAsString(), Universe.class);
    }

    private void deleteOne(String universe) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete(BASE_PATH + "/{universe}", universe).with(csrf()))
            .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
