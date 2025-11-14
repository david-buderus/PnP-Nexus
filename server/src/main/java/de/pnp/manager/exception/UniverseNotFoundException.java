package de.pnp.manager.exception;

import org.bson.types.ObjectId;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class UniverseNotFoundException extends ResponseStatusException {

    private final ObjectId universe;

    public UniverseNotFoundException(ObjectId universe) {
        super(NOT_FOUND, "Universe " + universe.toHexString() + " does not exist");
        this.universe = universe;
    }

    public ObjectId getUniverse() {
        return universe;
    }
}
