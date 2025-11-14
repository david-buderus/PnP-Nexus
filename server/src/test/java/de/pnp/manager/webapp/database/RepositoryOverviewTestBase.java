package de.pnp.manager.webapp.database;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.server.database.RepositoryBase;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Base class for testing overview pages for {@link RepositoryBase}.
 */
public abstract class RepositoryOverviewTestBase<T extends DatabaseObject> extends OverviewTestBase<T> {

    private T originalModifiedObject;

    /**
     * The repository which gets tested
     */
    protected RepositoryBase<T> repository;

    protected RepositoryOverviewTestBase(RepositoryBase<T> repository) {
        this.repository = repository;
    }

    @Override
    protected Collection<T> getTestObjects() {
        return repository.getAll(getUniverseId());
    }

    @Override
    protected Optional<T> getPersistedObject(T object) {
        return repository.getAll(getUniverseId()).stream().filter(o -> o.equals(object)).findFirst();
    }

    @Override
    protected Optional<T> getPersistedObject(ObjectId id) {
        return repository.get(getUniverseId(), id);
    }

    @Override
    protected ObjectId getModifyId() {
        return getOriginalModifiedObject().getId();
    }

    /**
     * Filter by which the object which will be edited can be found.
     */
    protected abstract Predicate<T> getOriginalModifiedFilter();

    /**
     * The modified object before it got changed.
     */
    protected T getOriginalModifiedObject() {
        if (originalModifiedObject == null) {
            originalModifiedObject = repository.getAll(getUniverseId()).stream().filter(getOriginalModifiedFilter())
                    .findFirst()
                    .orElseThrow();
        }
        return originalModifiedObject;
    }
}
