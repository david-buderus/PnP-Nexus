package de.pnp.manager.component;

import org.bson.types.ObjectId;

/**
 * Interfaces for DTOs with IDs.
 */
public interface IDTOWithId {

    /**
     * The ID of the DTO.
     *
     * @see DatabaseObject#getId()
     */
    ObjectId id();
}
