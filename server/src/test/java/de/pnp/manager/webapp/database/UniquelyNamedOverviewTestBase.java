package de.pnp.manager.webapp.database;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import org.bson.types.ObjectId;

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
    private T originalEditedObject;

    protected UniquelyNamedOverviewTestBase(R repository) {
        this.repository = repository;
    }

    /**
     * Name of the object which will be edited.
     */
    protected abstract String getEditObjectName();

    protected T getOriginalEditedObject() {
        if (originalEditedObject == null) {
            originalEditedObject = repository.get(getUniverseName(), getEditObjectName()).orElseThrow();
        }
        return originalEditedObject;
    }

    @Override
    protected Collection<T> getTestObjects() {
        return repository.getAll(getUniverseName());
    }

    @Override
    protected Comparator<T> getDefaultSort() {
        return Comparator.comparing(IUniquelyNamedDataObject::getName);
    }

    @Override
    protected String getIdentifier(T object) {
        return object.getName();
    }

    @Override
    protected Optional<T> getPersistedObject(T object) {
        return repository.get(getUniverseName(), object.getName());
    }

    @Override
    protected ObjectId getEditId() {
        return getOriginalEditedObject().getId();
    }
}
