package de.pnp.manager.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.component.Universe;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
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
 * Tests for {@link UniverseService}.
 */
@AutoConfigureMockMvc
@TestServer(EServerTestConfiguration.EMPTY)
public class UniverseServiceTest extends ServerTestBase {

    private static final String BASE_PATH = "/api/universes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(value = ADMIN, roles = "ADMIN")
    void testCreate() throws Exception {
        Universe exampleUniverse = new Universe("some-exampe", "Example Universe");
        Universe persistedExampleUniverse = create(exampleUniverse);
        assertThat(persistedExampleUniverse).isEqualTo(exampleUniverse);
        assertThat(getOne(exampleUniverse.getName())).isEqualTo(exampleUniverse);
        assertThat(getAll()).containsExactly(exampleUniverse);
    }

    @Test
    @WithMockUser(value = ADMIN, roles = "ADMIN")
    void testUpdate() throws Exception {
        Universe exampleUniverse = new Universe("some-exampe", "Example Universe");
        create(exampleUniverse);
        assertThat(getOne(exampleUniverse.getName())).isEqualTo(exampleUniverse);

        Universe changedUniverse = new Universe(exampleUniverse.getName(), "Other Title",
            exampleUniverse.getSettings());
        Universe oldUniverse = update(changedUniverse);
        assertThat(oldUniverse).isEqualTo(exampleUniverse);
        assertThat(getOne(exampleUniverse.getName())).isEqualTo(changedUniverse);
        assertThat(getAll()).containsExactly(changedUniverse);
    }

    @Test
    @WithMockUser(value = ADMIN, roles = "ADMIN")
    void deleteUniverse() throws Exception {
        Universe exampleUniverse = new Universe("some-exampe", "Example Universe");
        create(exampleUniverse);
        assertThat(getOne(exampleUniverse.getName())).isEqualTo(exampleUniverse);

        deleteOne(exampleUniverse.getName());

        assertThat(getAll()).isEmpty();
        assertThatThrownBy(() -> getOne(exampleUniverse.getName())).isInstanceOf(ResponseStatusException.class)
            .extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private List<Universe> getAll() throws Exception {
        String json = mockMvc.perform(get(BASE_PATH)).andExpect(status().isOk()).andReturn()
            .getResponse().getContentAsString();
        return objectMapper.readerForListOf(Universe.class).readValue(json);
    }

    private Universe getOne(String universe) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(BASE_PATH + "/{universe}", universe))
            .andReturn().getResponse();

        if (response.getStatus() >= 400) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readValue(response.getContentAsString(), Universe.class);
    }

    private Universe create(Universe universe) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                post(BASE_PATH).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(universe)))
            .andReturn().getResponse();

        return objectMapper.readValue(response.getContentAsString(), Universe.class);
    }

    private Universe update(Universe universe) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                put(BASE_PATH).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(universe)))
            .andReturn().getResponse();

        if (response.getStatus() >= 400) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }
        return objectMapper.readValue(response.getContentAsString(), Universe.class);
    }

    private void deleteOne(String universe) throws Exception {
        mockMvc.perform(delete(BASE_PATH + "/{universe}", universe).with(csrf()))
            .andExpect(status().isNoContent());
    }
}
