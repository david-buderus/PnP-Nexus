package de.pnp.manager.server.service;

import de.pnp.manager.component.Universe;
import de.pnp.manager.server.database.UniverseRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 *
 */
@RestController
@Validated
@RequestMapping("/api/universes")
public class UniverseService {
    /**
     * Message of the 404 exception thrown if the universe does not exist.
     */
    public static final String UNIVERSE_DOES_NOT_EXIST_EXCEPTION_MESSAGE = "Universe does not exist.";
    @Autowired
    private UniverseRepository universeRepository;

    @GetMapping()
    @Operation(summary = "Get all Universes", operationId = "getAllUniverses")
    public Collection<Universe> getUniverses() {
        return universeRepository.getAll();
    }

    @GetMapping("{universe}")
    @Operation(summary = "Get a universe", operationId = "getUniverse")
    public Universe getUniverse(@PathVariable String universe) {
        return universeRepository.get(universe).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, UNIVERSE_DOES_NOT_EXIST_EXCEPTION_MESSAGE));
    }

    @DeleteMapping("{universe}")
    @Operation(summary = "Delete a universe", operationId = "deleteUniverse")
    public boolean deleteUniverse(@PathVariable String universe) {
        return universeRepository.remove(universe);
    }

    @PostMapping()
    @Operation(summary = "Create a universe", operationId = "createUniverse")
    public Universe createUniverse(@RequestBody Universe universe) {
        return universeRepository.insert(universe);
    }

    @PutMapping()
    @Operation(summary = "Update a universe", operationId = "updateUniverse")
    public Universe updateUniverse(@RequestBody Universe newUniverse) {
        return universeRepository.update(newUniverse);
    }
}
