package de.pnp.manager.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * A delegate for a {@link MockMvc}.
 */
@Component
public class DelegateMvc {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DelegateMvc(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    /**
     * Get call that returns a list.
     */
    public <Obj> List<Obj> getList(Class<Obj> objClass, String path, Object... pathVariables) throws Exception {
        return getList(objClass, path, new LinkedMultiValueMap<>(), pathVariables);
    }

    /**
     * Get call that returns a list.
     */
    public <Obj> List<Obj> getList(Class<Obj> objClass, String path, LinkedMultiValueMap<String, String> params,
                                   Object... pathVariables) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(path, pathVariables).queryParams(params))
                .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readerForListOf(objClass).readValue(response.getContentAsString());
    }

    /**
     * Post call returns the inserted objects.
     */
    public <Obj> List<Obj> insertList(Class<Obj> objClass, String path, List<Obj> objects, Object... pathVariables)
            throws Exception {
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, objClass);

        MockHttpServletResponse response = mockMvc.perform(
                        post(path, pathVariables).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writerFor(collectionType).writeValueAsString(objects)))
                .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readerForListOf(objClass).readValue(response.getContentAsString());
    }

    /**
     * Delete call.
     */
    public void deleteObjects(String path, Object... pathVariables) throws Exception {
        deleteObjects(path, new LinkedMultiValueMap<>(), pathVariables);
    }

    /**
     * Delete call.
     */
    public void deleteObjects(String path, LinkedMultiValueMap<String, String> params,
                              Object... pathVariables) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete(path, pathVariables).with(csrf())
                .queryParams(params)).andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Get call for a single object
     */
    public <Obj> Obj getOne(Class<Obj> objClass, String path, Object... pathVariables) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(path, pathVariables))
                .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readValue(response.getContentAsString(), objClass);
    }

    /**
     * Put call for a single object.
     */
    public <Obj> Obj update(Class<Obj> objClass, String path, Obj obj, Object... pathVariables) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        put(path, pathVariables).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(obj)))
                .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }
        return objectMapper.readValue(response.getContentAsString(), objClass);
    }
}
