package de.pnp.manager.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.server.UniverseTestBase;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.utils.TestItemBuilder;
import de.pnp.manager.utils.TestItemBuilder.TestItemBuilderFactory;
import de.pnp.manager.utils.TestUpgradeBuilder;
import de.pnp.manager.utils.TestUpgradeBuilder.TestUpgradeBuilderFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@AutoConfigureMockMvc
public abstract class RepositoryServiceBaseTest<Obj extends DatabaseObject, Repo extends RepositoryBase<Obj>,
    Service extends RepositoryServiceBase<Obj, Repo>> extends UniverseTestBase {

    /**
     * The service which gets tested.
     */
    protected Service service;

    private final Class<Obj> objClass;

    /**
     * The base path used by the {@link #service}.
     */
    protected final String basePath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestItemBuilderFactory itemBuilder;

    @Autowired
    private TestUpgradeBuilderFactory upgradeBuilder;

    public RepositoryServiceBaseTest(Service service, Class<Obj> objClass) {
        this.service = service;
        this.objClass = objClass;
        basePath = extractBasePath();
    }

    @Test
    void soundnessCheck() {
        assertThat(createObjects()).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void testGetAll() throws Exception {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = insertAll(getUniverseName(), objects);

        assertThat(getAll(getUniverseName(), null)).containsExactlyInAnyOrderElementsOf(objects);
        assertThat(getAll(getUniverseName(), Collections.emptyList())).containsExactlyInAnyOrderElementsOf(objects);

        assertThat(getAll(getUniverseName(), persistedObjects.stream().map(DatabaseObject::getId).limit(2)
            .toList())).containsExactlyInAnyOrderElementsOf(persistedObjects.stream().limit(2).toList());
    }

    @Test
    void testDeleteAll() throws Exception {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = insertAll(getUniverseName(), objects);

        deleteAll(getUniverseName(), persistedObjects.stream().map(DatabaseObject::getId).limit(2).toList());
        assertThat(getAll(getUniverseName(), null)).containsExactlyInAnyOrderElementsOf(
            objects.stream().skip(2).toList());
    }

    @Test
    void testGet() throws Exception {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = insertAll(getUniverseName(), objects);

        assertThat(getOne(getUniverseName(), persistedObjects.stream().findFirst().orElseThrow().getId())).isEqualTo(
            objects.get(0));
    }

    @Test
    void testUpdate() throws Exception {
        List<Obj> objects = createObjects();
        Obj persistedObject = insertAll(getUniverseName(), List.of(objects.get(0))).stream().findFirst()
            .orElseThrow();

        assertThat(getOne(getUniverseName(), persistedObject.getId())).isEqualTo(persistedObject);
        Obj updatedObject = objects.get(1);
        assertThat(update(getUniverseName(), persistedObject.getId(), updatedObject)).isEqualTo(updatedObject);
        assertThat(getOne(getUniverseName(), persistedObject.getId())).isEqualTo(updatedObject);
    }

    @Test
    void testDelete() throws Exception {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = insertAll(getUniverseName(), objects);

        ObjectId deletedId = persistedObjects.stream().findFirst().orElseThrow().getId();
        deleteOne(getUniverseName(), deletedId);

        assertThat(getAll(getUniverseName(), null)).containsExactlyInAnyOrderElementsOf(
            objects.stream().skip(1).toList());
        assertThatThrownBy(() -> getOne(getUniverseName(), deletedId)).isInstanceOf(ResponseStatusException.class)
            .extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a list of {@link Obj objects} to use for the tests. The list need to have at least three objects.
     */
    protected abstract List<Obj> createObjects();

    /**
     * Wraps {@link RepositoryServiceBase#getAll(String, List)} in a REST call.
     */
    protected List<Obj> getAll(String universe, List<ObjectId> ids) throws Exception {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (ids != null) {
            map.addAll("ids", ids.stream().map(ObjectId::toHexString).toList());
        }
        String json = mockMvc.perform(get(basePath, universe).queryParams(map)).andExpect(status().isOk()).andReturn()
            .getResponse().getContentAsString();
        return objectMapper.readerForListOf(objClass).readValue(json);
    }

    /**
     * Wraps {@link RepositoryServiceBase#insertAll(String, List)} in a REST call.
     */
    protected List<Obj> insertAll(String universe, List<Obj> objects) throws Exception {
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, objClass);

        String json = mockMvc.perform(post(basePath, universe).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writerFor(collectionType).writeValueAsString(objects)))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        return objectMapper.readerForListOf(objClass).readValue(json);
    }

    /**
     * Wraps {@link RepositoryServiceBase#deleteAll(String, List)} in a REST call.
     */
    protected void deleteAll(String universe, List<ObjectId> ids) throws Exception {
        mockMvc.perform(delete(basePath, universe)
                .queryParam("ids", ids.stream().map(ObjectId::toHexString).toArray(String[]::new)))
            .andExpect(status().isNoContent());
    }

    /**
     * Wraps {@link RepositoryServiceBase#get(String, ObjectId)} in a REST call.
     */
    protected Obj getOne(String universe, ObjectId id) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(basePath + "/{id}", universe, id.toHexString()))
            .andReturn().getResponse();

        if (response.getStatus() >= 400) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readValue(response.getContentAsString(), objClass);
    }

    /**
     * Wraps {@link RepositoryServiceBase#update(String, ObjectId, DatabaseObject)} in a REST call.
     */
    protected Obj update(String universe, ObjectId id, Obj obj) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(put(basePath + "/{id}", universe, id.toHexString())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(obj)))
            .andReturn().getResponse();

        if (response.getStatus() >= 400) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }
        return objectMapper.readValue(response.getContentAsString(), objClass);
    }

    /**
     * Wraps {@link RepositoryServiceBase#delete(String, ObjectId)} in a REST call.
     */
    protected void deleteOne(String universe, ObjectId id) throws Exception {
        mockMvc.perform(delete(basePath + "/{id}", universe, id.toHexString()))
            .andExpect(status().isNoContent());
    }

    /**
     * Extracts the base path of the {@link #service}.
     */
    private String extractBasePath() {
        Class<?> serviceClass = service.getClass();

        do {
            RequestMapping requestMapping = serviceClass.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                return cleanupBasePath(requestMapping.value()[0]);
            }
            for (Class<?> classInterface : serviceClass.getInterfaces()) {
                requestMapping = classInterface.getAnnotation(RequestMapping.class);
                if (requestMapping != null) {
                    return cleanupBasePath(requestMapping.value()[0]);
                }
            }
            serviceClass = serviceClass.getSuperclass();
        } while (serviceClass != null);

        return fail("The service is not annotated with RequestMapping");
    }

    private String cleanupBasePath(String basePath) {
        if (!basePath.startsWith("/")) {
            basePath = "/" + basePath;
        }
        return basePath;
    }

    /**
     * A helper method to create {@link TestItemBuilder}.
     */
    protected TestItemBuilder createItem() {
        return itemBuilder.createItemBuilder(getUniverseName());
    }

    /**
     * A helper method to create {@link TestItemBuilder}.
     */
    protected TestUpgradeBuilder createUpgrade() {
        return upgradeBuilder.createUpgradeBuilder(getUniverseName());
    }
}