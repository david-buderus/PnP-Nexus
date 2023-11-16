package de.pnp.manager.server.service;

import static de.pnp.manager.security.SecurityConstants.ADMIN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.google.common.annotations.VisibleForTesting;
import de.pnp.manager.component.user.IGrantedAuthorityDTO;
import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.component.user.PnPUserDetails;
import de.pnp.manager.security.AdminRights;
import de.pnp.manager.server.contoller.UserController;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * Service to access {@link UserRepository} and {@link UserDetailsRepository}.
 */
@RestController
@Validated
@RequestMapping("/api/users")
public class UserService {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @PostMapping
    @AdminRights
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Create a user", operationId = "createUser")
    public void createUser(@RequestBody PnPUserCreation userCreation) {
        userController.createNewUser(userCreation);
    }

    @GetMapping("{username}")
    @PreAuthorize("hasRole('" + ADMIN + "') || #username == authentication.name")
    @Operation(summary = "Get a user", operationId = "getUser")
    public PnPUser getUser(@PathVariable String username) {
        return userRepository.getUser(username)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User " + username + " not found."));
    }

    @PutMapping("{username}")
    @PreAuthorize("hasRole('" + ADMIN + "') || #username == authentication.name")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a user", operationId = "updateUser")
    public void updateUser(@PathVariable String username, @RequestBody PnPUser user) {
        if (!Objects.equals(username, user.getUsername())) {
            throw new ResponseStatusException(BAD_REQUEST, "The username of the object does not match.");
        }
        userRepository.updateUser(user);
    }

    @DeleteMapping("{username}")
    @PreAuthorize("hasRole('" + ADMIN + "') || #username == authentication.name")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user", operationId = "removeUser")
    public void removeUser(@PathVariable String username) {
        if (!userController.removeUser(username)) {
            throw new ResponseStatusException(NOT_FOUND, "User " + username + " not found.");
        }
    }

    @PostMapping("{username}/password")
    @PreAuthorize("#username == authentication.name")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates the password of a user", operationId = "updatePassword")
    public void updatePassword(@PathVariable String username, @RequestBody PasswordChange passwordChange) {
        userDetailsRepository.updatePassword(username, passwordChange.oldPassword(), passwordChange.newPassword());
    }

    @PostMapping("{username}/permissions")
    @AdminRights
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates the permissions of a user", operationId = "updatePermissions")
    public void updatePermissions(@PathVariable String username,
        @RequestBody Collection<IGrantedAuthorityDTO> authorities) {
        userDetailsRepository.updateGrantedAuthority(username,
            authorities.stream().map(IGrantedAuthorityDTO::convert).toList());
    }

    @GetMapping("{username}/permissions")
    @PreAuthorize("hasRole('" + ADMIN + "') || #username == authentication.name")
    @Operation(summary = "Gets the permissions of a user", operationId = "getPermissions")
    public Collection<IGrantedAuthorityDTO> getPermissions(@PathVariable String username) {
        Optional<PnPUserDetails> user = userDetailsRepository.getUser(username);
        if (user.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "User " + username + " not found.");
        }
        return user.get().getAuthorities().stream().map(IGrantedAuthorityDTO::from).toList();
    }

    /**
     * DTO to update the password of a user.
     */
    @VisibleForTesting
    record PasswordChange(String oldPassword, String newPassword) {

    }
}
