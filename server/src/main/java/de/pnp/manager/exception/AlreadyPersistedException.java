package de.pnp.manager.exception;

import de.pnp.manager.component.DatabaseObject;
import java.util.Collection;
import java.util.Collections;

/**
 * A {@link RuntimeException} that gets thrown if a {@link DatabaseObject} gets persisted which
 * {@link DatabaseObject#isPersisted() is already persisted}.
 */
public class AlreadyPersistedException extends RuntimeException {

    private final Collection<? extends DatabaseObject> alreadyPersistedObjects;

    public AlreadyPersistedException(DatabaseObject object) {
        this(Collections.singleton(object));
    }

    public AlreadyPersistedException(Collection<? extends DatabaseObject> alreadyPersistedObjects) {
        this.alreadyPersistedObjects = alreadyPersistedObjects;
    }

    public Collection<? extends DatabaseObject> getAlreadyPersistedItems() {
        return alreadyPersistedObjects;
    }
}
