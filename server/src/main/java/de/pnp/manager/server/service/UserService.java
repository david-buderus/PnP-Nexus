package de.pnp.manager.server.service;

import com.google.common.annotations.VisibleForTesting;
import de.pnp.manager.component.user.*;
import de.pnp.manager.security.AdminRights;
import de.pnp.manager.server.contoller.UserController;
import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.database.UserPreferenceRepository;
import de.pnp.manager.server.database.UserRepository;
import de.pnp.manager.validation.Password;
import de.pnp.manager.validation.ValidCurrentPassword;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static de.pnp.manager.security.SecurityConstants.ADMIN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Service to access {@link UserRepository} and {@link UserDetailsRepository}.
 */
@RestController
@RequestMapping("/api/users")
public class UserService {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserPreferenceRepository preferenceRepository;

    @GetMapping("display-names")
    @Operation(summary = "Get all display names", operationId = "getDisplayNames")
    public Collection<String> getAllDisplayNames() {
        return userRepository.getAllUsers().stream().map(PnPUser::displayName).toList();
    }

    @GetMapping
    @AdminRights
    @Operation(summary = "Get all users", operationId = "getAllUsers")
    public Collection<PnPUser> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @PostMapping
    @AdminRights
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Create a user", operationId = "createUser")
    public void createUser(@Valid @RequestBody PnPUserCreation userCreation) {
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
    public void updateUser(@PathVariable String username, @Valid @RequestBody PnPUser user) {
        if (!Objects.equals(username, user.username())) {
            throw new ResponseStatusException(BAD_REQUEST, "The username of the object does not match.");
        }
        userRepository.updateUser(user);
    }

    @DeleteMapping("{username}")
    @PreAuthorize("hasRole('" + ADMIN + "') || #username == authentication.name")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user", operationId = "removeUser")
    public void removeUser(HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable String username) {
        if (!userController.removeUser(username)) {
            throw new ResponseStatusException(NOT_FOUND, "User " + username + " not found.");
        }
        if (username.equals(userDetails.getUsername())) {
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();
        }
    }

    @DeleteMapping
    @AdminRights
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete users", operationId = "removeUsers")
    public void removeUsers(@RequestParam List<String> usernames) {
        List<String> unknownUsers = new ArrayList<>();

        for (String username : usernames) {
            if (!userController.removeUser(username)) {
                unknownUsers.add(username);
            }
        }

        if (!unknownUsers.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Users [" + String.join(", ", unknownUsers) + "] not found.");
        }
    }

    @GetMapping("{username}/preferences")
    @PreAuthorize("#username == authentication.name")
    @Operation(summary = "Gets the user preferences", operationId = "getUserPreferences")
    public PnPUserPreference getPreferences(@PathVariable String username) {
        return preferenceRepository.getPreference(username)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User " + username + " not found."));
    }

    @PutMapping("{username}/preferences")
    @PreAuthorize("#username == authentication.name")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a user preferences", operationId = "updateUserPreferences")
    public void updatePreferences(@PathVariable String username, @Valid @RequestBody PnPUserPreference preference) {
        if (!Objects.equals(username, preference.username())) {
            throw new ResponseStatusException(BAD_REQUEST, "The username of the object does not match.");
        }
        preferenceRepository.updateUser(preference);
    }

    @PostMapping("{username}/permissions")
    @AdminRights
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates the permissions of a user", operationId = "updatePermissions")
    public void updatePermissions(@PathVariable String username,
                                  @Valid @RequestBody Collection<IGrantedAuthorityDTO> authorities) {
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
    public record PasswordChange(@ValidCurrentPassword String oldPassword, @Password String newPassword) {

    }
}
