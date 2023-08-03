package de.pnp.manager.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.server.database.UniverseTestBase;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class RepositoryServiceBaseTest<Obj extends DatabaseObject, Repo extends RepositoryBase<Obj>,
    Service extends RepositoryServiceBase<Obj, Repo>> extends UniverseTestBase {

    /**
     * The service which gets tested.
     */
    protected Service service;

    @Autowired
    private TestItemBuilderFactory itemBuilder;

    @Autowired
    private TestUpgradeBuilderFactory upgradeBuilder;

    public RepositoryServiceBaseTest(Service service) {
        this.service = service;
    }

    @Test
    void soundnessCheck() {
        assertThat(createObjects()).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void testGetAll() {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = service.insertAll(universeName, objects);

        assertThat(service.getAll(universeName, null)).containsExactlyInAnyOrderElementsOf(objects);
        assertThat(service.getAll(universeName, Collections.emptyList())).containsExactlyInAnyOrderElementsOf(objects);

        assertThat(service.getAll(universeName, persistedObjects.stream().map(DatabaseObject::getId).limit(2)
            .toList())).containsExactlyInAnyOrderElementsOf(persistedObjects.stream().limit(2).toList());
    }

    @Test
    void testDeleteAll() {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = service.insertAll(universeName, objects);

        service.deleteAll(universeName, persistedObjects.stream().map(DatabaseObject::getId).limit(2).toList());
        assertThat(service.getAll(universeName, null)).containsExactlyInAnyOrderElementsOf(
            objects.stream().skip(2).toList());
    }

    @Test
    void testGet() {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = service.insertAll(universeName, objects);

        assertThat(service.get(universeName, persistedObjects.stream().findFirst().orElseThrow().getId())).isEqualTo(
            objects.get(0));
    }

    @Test
    void testUpdate() {
        List<Obj> objects = createObjects();
        Obj persistedObject = service.insertAll(universeName, List.of(objects.get(0))).stream().findFirst()
            .orElseThrow();

        assertThat(service.get(universeName, persistedObject.getId())).isEqualTo(persistedObject);
        Obj updatedObject = objects.get(1);
        assertThat(service.update(universeName, persistedObject.getId(), updatedObject)).isEqualTo(updatedObject);
        assertThat(service.get(universeName, persistedObject.getId())).isEqualTo(updatedObject);
    }

    @Test
    void testDelete() {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = service.insertAll(universeName, objects);

        ObjectId deletedId = persistedObjects.stream().findFirst().orElseThrow().getId();
        service.delete(universeName, deletedId);

        assertThat(service.getAll(universeName, null)).containsExactlyInAnyOrderElementsOf(
            objects.stream().skip(1).toList());
        assertThatThrownBy(() -> service.get(universeName, deletedId)).isInstanceOf(ResponseStatusException.class)
            .extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a list of {@link Obj objects} to use for the tests. The list need to have at least three objects.
     */
    protected abstract List<Obj> createObjects();

    /**
     * A helper method to create {@link TestItemBuilder}.
     */
    protected TestItemBuilder createItem() {
        return itemBuilder.createItemBuilder(universeName);
    }

    /**
     * A helper method to create {@link TestItemBuilder}.
     */
    protected TestUpgradeBuilder createUpgrade() {
        return upgradeBuilder.createUpgradeBuilder(universeName);
    }
}