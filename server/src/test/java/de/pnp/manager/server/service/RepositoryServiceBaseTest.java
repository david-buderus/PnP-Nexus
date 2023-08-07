package de.pnp.manager.server.service;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.server.UniverseTestBase;
import de.pnp.manager.server.database.RepositoryBase;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class RepositoryServiceBaseTest<Obj extends DatabaseObject, Repo extends RepositoryBase<Obj>,
        Service extends RepositoryServiceBase<Obj, Repo>> extends UniverseTestBase {

    /**
     * The service which gets tested.
     */
    protected Service service;

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
        Collection<Obj> persistedObjects = service.insertAll(getUniverseName(), objects);

        assertThat(service.getAll(getUniverseName(), null)).containsExactlyInAnyOrderElementsOf(objects);
        assertThat(service.getAll(getUniverseName(), Collections.emptyList())).containsExactlyInAnyOrderElementsOf(objects);

        assertThat(service.getAll(getUniverseName(), persistedObjects.stream().map(DatabaseObject::getId).limit(2)
                .toList())).containsExactlyInAnyOrderElementsOf(persistedObjects.stream().limit(2).toList());
    }

    @Test
    void testDeleteAll() {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = service.insertAll(getUniverseName(), objects);

        service.deleteAll(getUniverseName(), persistedObjects.stream().map(DatabaseObject::getId).limit(2).toList());
        assertThat(service.getAll(getUniverseName(), null)).containsExactlyInAnyOrderElementsOf(
                objects.stream().skip(2).toList());
    }

    @Test
    void testGet() {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = service.insertAll(getUniverseName(), objects);

        assertThat(service.get(getUniverseName(), persistedObjects.stream().findFirst().orElseThrow().getId())).isEqualTo(
                objects.get(0));
    }

    @Test
    void testUpdate() {
        List<Obj> objects = createObjects();
        Obj persistedObject = service.insertAll(getUniverseName(), List.of(objects.get(0))).stream().findFirst()
                .orElseThrow();

        assertThat(service.get(getUniverseName(), persistedObject.getId())).isEqualTo(persistedObject);
        Obj updatedObject = objects.get(1);
        assertThat(service.update(getUniverseName(), persistedObject.getId(), updatedObject)).isEqualTo(updatedObject);
        assertThat(service.get(getUniverseName(), persistedObject.getId())).isEqualTo(updatedObject);
    }

    @Test
    void testDelete() {
        List<Obj> objects = createObjects();
        Collection<Obj> persistedObjects = service.insertAll(getUniverseName(), objects);

        ObjectId deletedId = persistedObjects.stream().findFirst().orElseThrow().getId();
        service.delete(getUniverseName(), deletedId);

        assertThat(service.getAll(getUniverseName(), null)).containsExactlyInAnyOrderElementsOf(
                objects.stream().skip(1).toList());
        assertThatThrownBy(() -> service.get(getUniverseName(), deletedId)).isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a list of {@link Obj objects} to use for the tests. The list need to have at least three objects.
     */
    protected abstract List<Obj> createObjects();
}