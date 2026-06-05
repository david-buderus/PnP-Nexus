package de.pnp.manager.server.service.universe;

import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.component.user.GrantedDatabaseObjectAuthority;
import de.pnp.manager.component.user.UserDatabaseObjectPermissionDTO;
import de.pnp.manager.exception.AlreadyPersistedException;
import de.pnp.manager.security.SecurityConstants;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.server.contoller.UserController;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.universe.UniverseRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Objects;

import static de.pnp.manager.security.SecurityConstants.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

    private final UniverseRepository universeRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final UserController userController;

    public UniverseService(@Autowired UniverseRepository universeRepository,
                           @Autowired UserDetailsRepository userDetailsRepository,
                           @Autowired UserController userController) {
        this.universeRepository = universeRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.userController = userController;
    }

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
    public Universe getUniverse(@PathVariable ObjectId universe) {
        return universeRepository.get(universe)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, UNIVERSE_DOES_NOT_EXIST_EXCEPTION_MESSAGE));
    }

    @DeleteMapping("{universe}")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a universe", operationId = "deleteUniverse")
    public void deleteUniverse(@PathVariable ObjectId universe) {
        if (!universeRepository.remove(universe)) {
            throw new ResponseStatusException(NOT_FOUND, UNIVERSE_DOES_NOT_EXIST_EXCEPTION_MESSAGE);
        }
        for (String username : userDetailsRepository.getAllUsernames()) {
            userDetailsRepository.removeGrantedDatabaseObjectAuthorities(username, universe);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('" + SecurityConstants.UNIVERSE_CREATOR + "')")
    @Operation(summary = "Create a universe", operationId = "createUniverse")
    public Universe createUniverse(@AuthenticationPrincipal UserDetails userDetails,
                                   @Valid @RequestBody Universe universe) {
        if (universe.isPersisted()) {
            throw new AlreadyPersistedException(universe);
        }
        Universe persistedUniverse = universeRepository.insert(universe);
        userDetailsRepository.addGrantedAuthority(userDetails.getUsername(),
                GrantedDatabaseObjectAuthority.ownerAuthority(persistedUniverse.getId()));
        return persistedUniverse;
    }

    @PutMapping("{universeId}")
    @PreAuthorize("hasRole('" + ADMIN + "') || hasPermission(#universeId, '" + UNIVERSE_TARGET_ID + "', '" + OWNER + "')")
    @Operation(summary = "Update a universe", operationId = "updateUniverse")
    public Universe updateUniverse(@PathVariable ObjectId universeId, @Valid @RequestBody Universe newUniverse) {
        if (!Objects.equals(newUniverse.getId(), universeId)) {
            throw new ResponseStatusException(BAD_REQUEST, "The universe path does not match the given universe name");
        }
        return universeRepository.update(newUniverse);
    }

    @PostMapping("{universe}/permission")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Add the given access right to the given user", operationId = "addUniversePermission")
    public void addPermission(@PathVariable ObjectId universe, @RequestParam @NotBlank String displayName,
                              @RequestParam(defaultValue = SecurityConstants.READ_ACCESS) String accessPermission) {
        userController.addGrantedAuthorityByDisplayName(displayName,
                GrantedDatabaseObjectAuthority.fromPermission(universe, accessPermission));
    }

    @DeleteMapping("{universe}/permission")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Removes all access rights to the universe from the given user", operationId = "removeUniversePermission")
    public void removePermission(@PathVariable ObjectId universe, @RequestParam String displayName) {
        userController.removeGrantedDatabaseObjectAuthoritiesByDisplayName(displayName, universe);
    }

    @GetMapping("{universe}/permission")
    @UniverseOwner
    @Operation(summary = "List all access rights of the universe", operationId = "getUniversePermissions")
    public Collection<UserDatabaseObjectPermissionDTO> getPermissions(@PathVariable ObjectId universe) {
        return userController.getAllUserWithUniversePermission(universe);
    }
}
