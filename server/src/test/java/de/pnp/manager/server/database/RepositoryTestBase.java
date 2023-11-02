package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.server.UniverseTestBase;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import de.pnp.manager.utils.TestItemBuilder;
import de.pnp.manager.utils.TestItemBuilder.TestItemBuilderFactory;
import de.pnp.manager.utils.TestUpgradeBuilder;
import de.pnp.manager.utils.TestUpgradeBuilder.TestUpgradeBuilderFactory;
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

    @Autowired
    private TestItemBuilderFactory itemBuilder;

    @Autowired
    private TestUpgradeBuilderFactory upgradeBuilder;

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
        E persistedObject = repository.insert(getUniverseName(), object);

        assertThat(persistedObject).isEqualTo(object);
        assertThat(repository.getAll(getUniverseName())).contains(object);
        assertThat(repository.get(getUniverseName(), persistedObject.getId())).contains(object);

        if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
            IUniquelyNamedDataObject namedObject = (IUniquelyNamedDataObject) object;
            Optional<?> optional = uniqueNameRepository.get(getUniverseName(), namedObject.getName());
            assertThat(optional).isNotEmpty();
            assertThat(optional.get()).isEqualTo(object);
        }
    }

    @Test
    void testRemove() {
        E object = repository.insert(getUniverseName(), createObject());
        assertThat(repository.getAll(getUniverseName())).contains(object);

        assertThat(repository.remove(getUniverseName(), object.getId())).isTrue();
        assertThat(repository.getAll(getUniverseName())).isEmpty();
        assertThat(repository.get(getUniverseName(), object.getId())).isEmpty();

        if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
            IUniquelyNamedDataObject namedObject = (IUniquelyNamedDataObject) object;
            assertThat(uniqueNameRepository.get(getUniverseName(), namedObject.getName())).isEmpty();
        }
    }

    @Test
    void testUpdate() {
        E object = repository.insert(getUniverseName(), createObject());
        assertThat(repository.getAll(getUniverseName())).contains(object);

        E change = createSlightlyChangeObject();
        E persistedChange = repository.update(getUniverseName(), object.getId(), change);
        assertThat(persistedChange).isEqualTo(change);
        assertThat(persistedChange.getId()).isEqualTo(object.getId());
        assertThat(repository.getAll(getUniverseName())).contains(change);
        assertThat(repository.get(getUniverseName(), persistedChange.getId())).contains(change);

        if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
            IUniquelyNamedDataObject namedChange = (IUniquelyNamedDataObject) persistedChange;
            Optional<?> optional = uniqueNameRepository.get(getUniverseName(), namedChange.getName());
            assertThat(optional).isNotEmpty();
            assertThat(optional.get()).isEqualTo(change);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void testInsertAll() {
        List<E> objects = createMultipleObjects();
        Collection<E> insertedObjects = repository.insertAll(getUniverseName(), objects);

        assertThat(insertedObjects).containsExactlyInAnyOrderElementsOf(objects);
        assertThat(repository.getAll(getUniverseName())).containsExactlyInAnyOrderElementsOf(objects);
        assertThat(repository.getAll(getUniverseName(),
            insertedObjects.stream().map(E::getId).toList())).containsExactlyInAnyOrderElementsOf(
            objects);

        if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
            assertThat(uniqueNameRepository.getAllByName(getUniverseName(),
                insertedObjects.stream().map(obj -> (IUniquelyNamedDataObject) obj)
                    .map(IUniquelyNamedDataObject::getName)
                    .toList())).map(obj -> (E) obj).containsExactlyInAnyOrderElementsOf(
                objects);
        }
    }

    @Test
    void testRemoveAll() {
        Collection<E> objects = repository.insertAll(getUniverseName(), createMultipleObjects());
        assertThat(repository.getAll(getUniverseName())).containsAll(objects);

        List<ObjectId> objectIds = objects.stream().map(E::getId).toList();
        assertThat(repository.removeAll(getUniverseName(), objectIds)).isTrue();
        assertThat(repository.getAll(getUniverseName(), objectIds)).isEmpty();
        assertThat(repository.getAll(getUniverseName())).isEmpty();

        if (repository instanceof IUniquelyNamedRepository<?> uniqueNameRepository) {
            assertThat(uniqueNameRepository.getAllByName(getUniverseName(),
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
        assertThat(linkRepository.get(getUniverseName(), originalLink.getId())).as(
            "The original link has to be already persisted.").isNotEmpty();

        E persistedObject = repository.insert(getUniverseName(), object);
        assertThat(repository.get(getUniverseName(), persistedObject.getId())).contains(object);

        linkRepository.update(getUniverseName(), originalLink.getId(), changedLink);
        assertThat(linkRepository.get(getUniverseName(), originalLink.getId())).contains(changedLink);

        Optional<E> optional = repository.get(getUniverseName(), persistedObject.getId());
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
        assertThat(linkRepository.getAll(getUniverseName())).as(
            "The original links have to be already persisted.").containsAll(originalLinks);

        E persistedObject = repository.insert(getUniverseName(), object);
        assertThat(repository.get(getUniverseName(), persistedObject.getId())).contains(object);

        for (Link originalLink : originalLinks) {
            Link changedLink = changedLinks.get(originalLink);

            if (changedLink != null) {
                linkRepository.update(getUniverseName(), originalLink.getId(), changedLink);
                assertThat(linkRepository.get(getUniverseName(), originalLink.getId())).contains(changedLink);
            } else {
                linkRepository.remove(getUniverseName(), originalLink.getId());
                assertThat(linkRepository.get(getUniverseName(), originalLink.getId())).isEmpty();
            }
        }

        Optional<E> optional = repository.get(getUniverseName(), persistedObject.getId());
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
