package de.pnp.manager.webapp.database;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.Optional;

/**
 * Base class for testing overview pages for {@link IUniquelyNamedRepository}.
 */
public abstract class UniquelyNamedOverviewTestBase<T extends DatabaseObject & IUniquelyNamedDataObject,
        R extends RepositoryBase<T> & IUniquelyNamedRepository<T>> extends OverviewTestBase<T> {

    /**
     * The repository used for the testing.
     */
    protected final R repository;

    /**
     * Cache for the original edit object.
     */
    private T originalModifyObject;

    protected UniquelyNamedOverviewTestBase(R repository) {
        this.repository = repository;
    }

    /**
     * Name of the object which will be edited.
     */
    protected abstract String getEditObjectName();

    /**
     * The modified object before it got changed.
     */
    protected T getOriginalModifyObject() {
        if (originalModifyObject == null) {
            originalModifyObject = repository.get(getUniverseId(), getEditObjectName()).orElseThrow();
        }
        return originalModifyObject;
    }

    @Override
    protected Collection<T> getTestObjects() {
        return repository.getAll(getUniverseId());
    }

    @Override
    protected String getIdentifier(T object) {
        return object.getName();
    }

    @Override
    protected Optional<T> getPersistedObject(T object) {
        return repository.get(getUniverseId(), object.getName());
    }

    @Override
    protected Optional<T> getPersistedObject(ObjectId id) {
        return repository.get(getUniverseId(), id);
    }

    @Override
    protected ObjectId getModifyId() {
        return getOriginalModifyObject().getId();
    }
}
