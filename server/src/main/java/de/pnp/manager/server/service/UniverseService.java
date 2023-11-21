package de.pnp.manager.server.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.component.user.GrantedUniverseAuthority;
import de.pnp.manager.security.SecurityConstants;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.server.database.UniverseRepository;
import de.pnp.manager.server.database.UserDetailsRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Collection;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @GetMapping
    @PostFilter("hasRole('" + SecurityConstants.ADMIN + "') || hasPermission(filterObject, '"
        + SecurityConstants.READ_ACCESS + "')")
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
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a universe", operationId = "deleteUniverse")
    public void deleteUniverse(@PathVariable String universe) {
        if (!universeRepository.remove(universe)) {
            throw new ResponseStatusException(NOT_FOUND, UNIVERSE_DOES_NOT_EXIST_EXCEPTION_MESSAGE);
        }
        for (String username : userDetailsRepository.getAllUsernames()) {
            userDetailsRepository.removeGrantedUniverseAuthorities(username, universe);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('" + SecurityConstants.UNIVERSE_CREATOR + "')")
    @Operation(summary = "Create a universe", operationId = "createUniverse")
    public Universe createUniverse(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Universe universe) {
        Universe persistedUniverse = universeRepository.insert(universe);
        userDetailsRepository.addGrantedAuthority(userDetails.getUsername(),
            GrantedUniverseAuthority.ownerAuthority(persistedUniverse.getName()));
        return persistedUniverse;
    }

    @PutMapping("{universe}")
    @UniverseOwner
    @Operation(summary = "Update a universe", operationId = "updateUniverse")
    public Universe updateUniverse(@PathVariable String universe, @RequestBody Universe newUniverse) {
        if (!Objects.equals(newUniverse.getName(), universe)) {
            throw new ResponseStatusException(BAD_REQUEST, "The universe path does not match the given universe name");
        }
        return universeRepository.update(newUniverse);
    }

    @PostMapping("{universe}/permission")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Add the given access right to the given user", operationId = "addPermission")
    public void addPermission(@PathVariable String universe, @RequestParam String username,
        @RequestParam(defaultValue = SecurityConstants.READ_ACCESS) String accessPermission) {
        userDetailsRepository.addGrantedAuthority(username,
            GrantedUniverseAuthority.fromPermission(universe, accessPermission));
    }

    @DeleteMapping("{universe}/permission")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Removes all access rights to the universe from the given user", operationId = "removePermission")
    public void removePermission(@PathVariable String universe, @RequestParam String username) {
        userDetailsRepository.removeGrantedUniverseAuthorities(username, universe);
    }
}
