package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import de.pnp.manager.utils.TestItemBuilder;
import de.pnp.manager.utils.TestItemBuilder.TestItemBuilderFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A base class for {@link RepositoryBase repository} tests.
 * <p>
 * It tests the basic functionalities like insert, get, remove, update.
 */
public abstract class RepositoryTestBase<E extends DatabaseObject, Repo extends RepositoryBase<E>> extends
    UniverseTestBase {

    /**
     * A factory to create {@link TestItemBuilder}.
     */
    @Autowired
    protected TestItemBuilderFactory itemBuilder;

    /**
     * The main repository for the test.
     */
    protected Repo repository;

    public RepositoryTestBase(Repo repository) {
        this.repository = repository;
    }

    @Test
    void testInsert() {
        E object = createObject();
        E persistedObject = repository.insert(universeName, object);

        assertThat(persistedObject).isEqualTo(object);
        assertThat(repository.getAll(universeName)).contains(object);
        assertThat(repository.get(universeName, persistedObject.getId())).contains(object);

        if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
            IUniquelyNamedDataObject namedObject = (IUniquelyNamedDataObject) object;
            Optional<?> optional = uniqueNameRepository.get(universeName, namedObject.getName());
            assertThat(optional).isNotEmpty();
            assertThat(optional.get()).isEqualTo(object);
        }
    }

    @Test
    void testRemove() {
        E object = repository.insert(universeName, createObject());
        assertThat(repository.getAll(universeName)).contains(object);

        assertThat(repository.remove(universeName, object.getId())).isTrue();
        assertThat(repository.getAll(universeName)).isEmpty();
        assertThat(repository.get(universeName, object.getId())).isEmpty();

        if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
            IUniquelyNamedDataObject namedObject = (IUniquelyNamedDataObject) object;
            assertThat(uniqueNameRepository.get(universeName, namedObject.getName())).isEmpty();
        }
    }

    @Test
    void testUpdate() {
        E object = repository.insert(universeName, createObject());
        assertThat(repository.getAll(universeName)).contains(object);

        E change = createSlightlyChangeObject();
        E persistedChange = repository.update(universeName, object.getId(), change);
        assertThat(persistedChange).isEqualTo(change);
        assertThat(persistedChange.getId()).isEqualTo(object.getId());
        assertThat(repository.getAll(universeName)).contains(change);
        assertThat(repository.get(universeName, persistedChange.getId())).contains(change);

        if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
            IUniquelyNamedDataObject namedChange = (IUniquelyNamedDataObject) persistedChange;
            Optional<?> optional = uniqueNameRepository.get(universeName, namedChange.getName());
            assertThat(optional).isNotEmpty();
            assertThat(optional.get()).isEqualTo(change);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void testInsertAll() {
        List<E> objects = createMultipleObjects();
        Collection<E> insertedObjects = repository.insertAll(universeName, objects);

        assertThat(insertedObjects).containsExactlyInAnyOrderElementsOf(objects);
        assertThat(repository.getAll(universeName)).containsExactlyInAnyOrderElementsOf(objects);
        assertThat(repository.getAll(universeName,
            insertedObjects.stream().map(E::getId).toList())).containsExactlyInAnyOrderElementsOf(
            objects);

        if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
            assertThat(uniqueNameRepository.getAllByName(universeName,
                insertedObjects.stream().map(obj -> (IUniquelyNamedDataObject) obj)
                    .map(IUniquelyNamedDataObject::getName)
                    .toList())).map(obj -> (E) obj).containsExactlyInAnyOrderElementsOf(
                objects);
        }
    }

    @Test
    void testRemoveAll() {
        Collection<E> objects = repository.insertAll(universeName, createMultipleObjects());
        assertThat(repository.getAll(universeName)).containsAll(objects);

        List<ObjectId> objectIds = objects.stream().map(E::getId).toList();
        assertThat(repository.removeAll(universeName, objectIds)).isTrue();
        assertThat(repository.getAll(universeName, objectIds)).isEmpty();
        assertThat(repository.getAll(universeName)).isEmpty();

        if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
            assertThat(uniqueNameRepository.getAllByName(universeName,
                objects.stream().map(obj -> (IUniquelyNamedDataObject) obj)
                    .map(IUniquelyNamedDataObject::getName).toList())).isEmpty();
        }
    }

    /**
     * A helper method to tests links between to repositories.
     * <p>
     * The originalLink needs to be persisted.
     */
    @SuppressWarnings("unchecked")
    protected <A extends E, Link extends DatabaseObject> void testRepositoryLink(
        Function<A, Link> linkGetter,
        RepositoryBase<Link> linkRepository, A object, Link originalLink,
        Link changedLink) {
        assertThat(linkRepository.get(universeName, originalLink.getId())).as(
            "The original link has to be already persisted.").isNotEmpty();

        E persistedObject = repository.insert(universeName, object);
        assertThat(repository.get(universeName, persistedObject.getId())).contains(object);

        linkRepository.update(universeName, originalLink.getId(), changedLink);
        assertThat(linkRepository.get(universeName, originalLink.getId())).contains(changedLink);

        Optional<E> optional = repository.get(universeName, persistedObject.getId());
        assertThat(optional).isNotEmpty();
        assertThat(linkGetter.apply((A) optional.get())).isEqualTo(changedLink);
    }

    /**
     * A helper method to test links between two repositories.
     * <p>
     * The originalLinks need to be persisted.
     */
    @SuppressWarnings("unchecked")
    protected <A extends E, Link extends DatabaseObject> void testRepositoryCollectionLink(
        Function<A, Collection<Link>> linkGetter,
        RepositoryBase<Link> linkRepository, A object, Collection<Link> originalLinks,
        Map<Link, Link> changedLinks) {
        assertThat(linkRepository.getAll(universeName)).as(
            "The original links have to be already persisted.").containsAll(originalLinks);

        E persistedObject = repository.insert(universeName, object);
        assertThat(repository.get(universeName, persistedObject.getId())).contains(object);

        for (Link originalLink : originalLinks) {
            Link changedLink = changedLinks.get(originalLink);

            if (changedLink != null) {
                linkRepository.update(universeName, originalLink.getId(), changedLink);
                assertThat(linkRepository.get(universeName, originalLink.getId())).contains(changedLink);
            } else {
                linkRepository.remove(universeName, originalLink.getId());
                assertThat(linkRepository.get(universeName, originalLink.getId())).isEmpty();
            }
        }

        Optional<E> optional = repository.get(universeName, persistedObject.getId());
        assertThat(optional).isNotEmpty();
        assertThat(linkGetter.apply((A) optional.get())).containsExactlyInAnyOrderElementsOf(
            changedLinks.values());
    }

    /**
     * A supplier for an {@link DatabaseObject} to use in the tests.
     */
    protected abstract E createObject();

    /**
     * A supplier for an {@link DatabaseObject} that is a slight modification of {@link #createObject()} to use in the
     * tests.
     * <p>
     * This method gets always called after {@link #createObject()}. That means you can reuse objects that were
     * persisted in other repositories in the other method.
     */
    protected abstract E createSlightlyChangeObject();

    /**
     * A supplier for a collection of {@link DatabaseObject} to use in the tests.
     * <p>
     * It must be possible to add all objects in the collection without a conflict.
     */
    protected abstract List<E> createMultipleObjects();
}
