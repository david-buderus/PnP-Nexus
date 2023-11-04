package de.pnp.manager.server.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import de.pnp.manager.component.Universe;
import de.pnp.manager.security.AdminRights;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.security.UniverseWrite;
import de.pnp.manager.server.database.UniverseRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service to access {@link UniverseRepository}.
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

    @GetMapping
    @PostFilter("hasRole('ADMIN') || hasPermission(filterObject.name, 'UNIVERSE', 'READ')")
    @Operation(summary = "Get all Universes", operationId = "getAllUniverses")
    public Collection<Universe> getUniverses() {
        return universeRepository.getAll();
    }

    @GetMapping("{universe}")
    @UniverseRead
    @Operation(summary = "Get a universe", operationId = "getUniverse")
    public Universe getUniverse(@PathVariable String universe) {
        return universeRepository.get(universe)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, UNIVERSE_DOES_NOT_EXIST_EXCEPTION_MESSAGE));
    }

    @DeleteMapping("{universe}")
    @AdminRights
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a universe", operationId = "deleteUniverse")
    public void deleteUniverse(@PathVariable String universe) {
        if (!universeRepository.remove(universe)) {
            throw new ResponseStatusException(NOT_FOUND, UNIVERSE_DOES_NOT_EXIST_EXCEPTION_MESSAGE);
        }
    }

    @PostMapping
    @AdminRights
    @Operation(summary = "Create a universe", operationId = "createUniverse")
    public Universe createUniverse(@RequestBody Universe universe) {
        return universeRepository.insert(universe);
    }

    @PutMapping
    @UniverseWrite
    @Operation(summary = "Update a universe", operationId = "updateUniverse")
    public Universe updateUniverse(@RequestBody Universe newUniverse) {
        return universeRepository.update(newUniverse);
    }
}
