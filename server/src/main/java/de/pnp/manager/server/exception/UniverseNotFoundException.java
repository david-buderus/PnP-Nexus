package de.pnp.manager.server.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.server.ResponseStatusException;

public class UniverseNotFoundException extends ResponseStatusException {

    private final String universe;

    public UniverseNotFoundException(String universe) {
        super(NOT_FOUND, "Universe " + universe + " does not exist");
        this.universe = universe;
    }

    public String getUniverse() {
        return universe;
    }
}
