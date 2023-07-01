package de.pnp.manager.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * A base class for objects in a database with an id.
 */
public abstract class DatabaseObject {

    /**
     * The unique id of this object.
     */
    @Id
    private final ObjectId id;

    protected DatabaseObject(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    /**
     * Checks whether this object is already persisted in a database.
     */
    @JsonIgnore
    public boolean isPersisted() {
        return id != null;
    }
}
