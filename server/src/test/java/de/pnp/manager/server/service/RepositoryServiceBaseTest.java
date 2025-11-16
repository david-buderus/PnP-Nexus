package de.pnp.manager.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.user.GrantedDatabaseObjectAuthority;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.security.SecurityConstants;
import de.pnp.manager.server.ManipulatesMetadata;
import de.pnp.manager.server.UniverseTestBase;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.utils.TestItemBuilder;
import de.pnp.manager.utils.TestItemBuilder.TestItemBuilderFactory;
import de.pnp.manager.utils.TestSecondaryAttributeBuilder;
import de.pnp.manager.utils.TestSecondaryAttributeBuilder.TestSecondaryAttributeBuilderFactory;
import de.pnp.manager.utils.TestSpellBuilder;
import de.pnp.manager.utils.TestSpellBuilder.TestSpellBuilderFactory;
import de.pnp.manager.utils.TestUpgradeBuilder;
import de.pnp.manager.utils.TestUpgradeBuilder.TestUpgradeBuilderFactory;
import org.bson.types.ObjectId;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static de.pnp.manager.server.service.ServiceTestUtils.assertForbidden;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ManipulatesMetadata
@AutoConfigureMockMvc
public abstract class RepositoryServiceBaseTest<Obj extends DatabaseObject, Repo extends RepositoryBase<Obj>,
        Service extends RepositoryServiceBase<Obj, Repo>> extends UniverseTestBase {

    private static final String USER = "test-user";

    /**
     * The service which gets tested.
     */
    protected Service service;

    /**
     * The repository behind the service which gets tested.
     */
    protected Repo repository;

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

    @Autowired
    private TestSecondaryAttributeBuilderFactory secondaryAttributeBuilder;

    @Autowired
    private TestSpellBuilderFactory spellBuilder;

    protected RepositoryServiceBaseTest(Service service, Repo repository, Class<Obj> objClass) {
        this.service = service;
        this.objClass = objClass;
        this.repository = repository;
        basePath = extractBasePath();
    }

    @Test
    @WithMockUser(roles = SecurityConstants.ADMIN)
    void soundnessCheck() {
        assertThat(createObjects()).hasSizeGreaterThanOrEqualTo(3);
    }

    @Nested
    @DisplayName("as admin")
    class AsAdminTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(
                    PnPUserCreation.simple(USER, List.of(new SimpleGrantedAuthority(SecurityConstants.ADMIN_ROLE))));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGetAll() throws Exception {
            runGetAllTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            runGetTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testInsert() throws Exception {
            runInsertTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDeleteAll() throws Exception {
            runDeleteAllTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() throws Exception {
            runDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() throws Exception {
            runUpdateTest();
        }
    }

    @Nested
    @DisplayName("as owner")
    class AsOwnerTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(
                    PnPUserCreation.simple(USER, List.of(GrantedDatabaseObjectAuthority.ownerAuthority(getUniverseId()))));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGetAll() throws Exception {
            runGetAllTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            runGetTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testInsert() throws Exception {
            runInsertTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDeleteAll() throws Exception {
            runDeleteAllTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() throws Exception {
            runDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() throws Exception {
            runUpdateTest();
        }
    }

    @Nested
    @DisplayName("as writer")
    class AsWriterTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(
                    PnPUserCreation.simple(USER, List.of(GrantedDatabaseObjectAuthority.writeAuthority(getUniverseId()))));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGetAll() throws Exception {
            runGetAllTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            runGetTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testInsert() throws Exception {
            runInsertTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDeleteAll() throws Exception {
            runDeleteAllTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() throws Exception {
            runDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() throws Exception {
            runUpdateTest();
        }
    }

    @Nested
    @DisplayName("as reader")
    class AsReaderTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(
                    PnPUserCreation.simple(USER, List.of(GrantedDatabaseObjectAuthority.readAuthority(getUniverseId()))));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGetAll() throws Exception {
            runGetAllTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() throws Exception {
            runGetTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testInsert() {
            runNotAllowedInsertTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDeleteAll() {
            runNotAllowedDeleteAllTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() {
            runNotAllowedDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() {
            runNotAllowedUpdateTest();
        }
    }

    @Nested
    @DisplayName("as no rights")
    class AsNoRightsTest {

        @BeforeEach
        protected void setup() {
            userController.createNewUser(
                    new PnPUserCreation(USER, USER, USER, null, List.of()));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGetAll() {
            List<Obj> objects = createObjects();
            Collection<Obj> persistedObjects = repository.insertAll(getUniverseId(), objects);

            assertForbidden(() -> getAll(getUniverseId(), null));
            assertForbidden(() -> getAll(getUniverseId(), Collections.emptyList()));
            assertForbidden(
                    () -> getAll(getUniverseId(), persistedObjects.stream().map(DatabaseObject::getId).limit(2)
                            .toList()));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testGet() {
            List<Obj> objects = createObjects();
            Collection<Obj> persistedObjects = repository.insertAll(getUniverseId(), objects);

            assertForbidden(
                    () -> getOne(getUniverseId(), persistedObjects.stream().findFirst().orElseThrow().getId()));
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testInsert() {
            runNotAllowedInsertTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDeleteAll() {
            runNotAllowedDeleteAllTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testDelete() {
            runNotAllowedDeleteTest();
        }

        @Test
        @WithUserDetails(value = USER, setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void testUpdate() {
            runNotAllowedUpdateTest();
        }
    }

    private void runGetAllTest() throws Exception {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = repository.insertAll(getUniverseId(), objects);

        assertThat(getAll(getUniverseId(), null)).containsExactlyInAnyOrderElementsOf(objects);
        assertThat(getAll(getUniverseId(), Collections.emptyList())).containsExactlyInAnyOrderElementsOf(objects);

        assertThat(getAll(getUniverseId(), persistedObjects.stream().map(DatabaseObject::getId).limit(2)
                .toList())).containsExactlyInAnyOrderElementsOf(persistedObjects.stream().limit(2).toList());
    }

    private void runGetTest() throws Exception {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = repository.insertAll(getUniverseId(), objects);

        assertThat(getOne(getUniverseId(), persistedObjects.stream().findFirst().orElseThrow().getId())).isEqualTo(
                objects.getFirst());
    }

    private void runInsertTest() throws Exception {
        List<Obj> objects = createObjects();
        List<Obj> persistedObjects = insertAll(getUniverseId(), objects);
        assertThat(persistedObjects).isEqualTo(objects);
        assertThat(getAll(getUniverseId(), null)).containsExactlyInAnyOrderElementsOf(objects);
    }

    private void runNotAllowedInsertTest() {
        List<Obj> objects = createObjects();
        assertForbidden(() -> insertAll(getUniverseId(), objects));
        assertThat(repository.getAll(getUniverseId())).isEmpty();
    }

    private void runDeleteAllTest() throws Exception {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = repository.insertAll(getUniverseId(), objects);

        deleteAll(getUniverseId(), persistedObjects.stream().map(DatabaseObject::getId).limit(2).toList());
        assertThat(getAll(getUniverseId(), null)).containsExactlyInAnyOrderElementsOf(
                objects.stream().skip(2).toList());
    }

    private void runNotAllowedDeleteAllTest() {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = repository.insertAll(getUniverseId(), objects);

        assertForbidden(
                () -> deleteAll(getUniverseId(), persistedObjects.stream().map(DatabaseObject::getId).limit(2).toList()));
        assertThat(repository.getAll(getUniverseId())).containsExactlyInAnyOrderElementsOf(persistedObjects);
    }

    private void runDeleteTest() throws Exception {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = repository.insertAll(getUniverseId(), objects);

        ObjectId deletedId = persistedObjects.stream().findFirst().orElseThrow().getId();
        deleteOne(getUniverseId(), deletedId);

        assertThat(getAll(getUniverseId(), null)).containsExactlyInAnyOrderElementsOf(
                objects.stream().skip(1).toList());
        assertThatThrownBy(() -> getOne(getUniverseId(), deletedId)).isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private void runNotAllowedDeleteTest() {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = repository.insertAll(getUniverseId(), objects);
        ObjectId deletedId = persistedObjects.stream().findFirst().orElseThrow().getId();

        assertForbidden(() -> deleteOne(getUniverseId(), deletedId));
        assertThat(repository.getAll(getUniverseId())).containsExactlyInAnyOrderElementsOf(persistedObjects);
    }

    private void runUpdateTest() throws Exception {
        List<Obj> objects = createObjects();
        Obj persistedObject = repository.insertAll(getUniverseId(), List.of(objects.get(0))).stream().findFirst()
                .orElseThrow();

        assertThat(getOne(getUniverseId(), persistedObject.getId())).isEqualTo(persistedObject);
        Obj updatedObject = objects.get(1);
        assertThat(update(getUniverseId(), persistedObject.getId(), updatedObject)).isEqualTo(updatedObject);
        assertThat(getOne(getUniverseId(), persistedObject.getId())).isEqualTo(updatedObject);
    }

    private void runNotAllowedUpdateTest() {
        List<Obj> objects = createObjects();
        Obj persistedObject = repository.insertAll(getUniverseId(), List.of(objects.get(0))).stream().findFirst()
                .orElseThrow();
        Obj updatedObject = objects.get(1);
        assertForbidden(() -> update(getUniverseId(), persistedObject.getId(), updatedObject));
        assertThat(repository.get(getUniverseId(), persistedObject.getId()).orElseThrow()).isEqualTo(persistedObject);
    }

    /**
     * Creates a list of {@link Obj objects} to use for the tests. The list need to have at least three objects.
     */
    protected abstract List<Obj> createObjects();

    /**
     * Wraps {@link RepositoryServiceBase#getAll(ObjectId, List)} in a REST call.
     */
    protected List<Obj> getAll(ObjectId universe, List<ObjectId> ids) throws Exception {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (ids != null) {
            map.addAll("ids", ids.stream().map(ObjectId::toHexString).toList());
        }
        MockHttpServletResponse response = mockMvc.perform(get(basePath, universe).queryParams(map))
                .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readerForListOf(objClass).readValue(response.getContentAsString());
    }

    /**
     * Wraps {@link RepositoryServiceBase#insertAll(ObjectId, List)} in a REST call.
     */
    protected List<Obj> insertAll(ObjectId universe, List<Obj> objects) throws Exception {
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, objClass);

        MockHttpServletResponse response = mockMvc.perform(
                        post(basePath, universe).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writerFor(collectionType).writeValueAsString(objects)))
                .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readerForListOf(objClass).readValue(response.getContentAsString());
    }

    /**
     * Wraps {@link RepositoryServiceBase#deleteAll(ObjectId, List)} in a REST call.
     */
    protected void deleteAll(ObjectId universe, List<ObjectId> ids) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete(basePath, universe).with(csrf())
                        .queryParam("ids", ids.stream().map(ObjectId::toHexString).toArray(String[]::new)))
                .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Wraps {@link RepositoryServiceBase#get(ObjectId, ObjectId)} in a REST call.
     */
    protected Obj getOne(ObjectId universe, ObjectId id) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(basePath + "/{id}", universe, id.toHexString()))
                .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        return objectMapper.readValue(response.getContentAsString(), objClass);
    }

    /**
     * Wraps {@link RepositoryServiceBase#update(ObjectId, ObjectId, DatabaseObject)} in a REST call.
     */
    protected Obj update(ObjectId universe, ObjectId id, Obj obj) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        put(basePath + "/{id}", universe, id.toHexString()).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(obj)))
                .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }
        return objectMapper.readValue(response.getContentAsString(), objClass);
    }

    /**
     * Wraps {@link RepositoryServiceBase#delete(ObjectId, ObjectId)} in a REST call.
     */
    protected void deleteOne(ObjectId universe, ObjectId id) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        delete(basePath + "/{id}", universe, id.toHexString()).with(csrf()))
                .andReturn().getResponse();

        if (response.getStatus() >= 300) {
            throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
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

        return fail("The service " + service.getClass().getSimpleName() + " is not annotated with RequestMapping");
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
        return itemBuilder.createItemBuilder(getUniverseId());
    }

    /**
     * A helper method to create {@link TestItemBuilder}.
     */
    protected TestUpgradeBuilder createUpgrade() {
        return upgradeBuilder.createUpgradeBuilder(getUniverseId());
    }

    /**
     * A helper method to create {@link TestSecondaryAttributeBuilder}.
     */
    protected TestSecondaryAttributeBuilder createSecondaryAttribute() {
        return secondaryAttributeBuilder.createAttributeBuilder(getUniverseId());
    }

    /**
     * A helper method to create {@link TestSpellBuilder}.
     */
    protected TestSpellBuilder createSpell() {
        return spellBuilder.createSpellBuilder(getUniverseId());
    }
}