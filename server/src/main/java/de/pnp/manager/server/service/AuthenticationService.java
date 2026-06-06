package de.pnp.manager.server.service;

import de.pnp.manager.server.database.UserDetailsRepository;
import de.pnp.manager.server.service.UserService.PasswordChange;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Service to authenticate.
 */
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationService {

    private final UserDetailsRepository userDetailsRepository;

    public AuthenticationService(@Autowired UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @GetMapping("current-user")
    @Operation(summary = "Returns the username of the authenticated user", operationId = "getUsername")
    public String getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getUsername();
    }

    @PostMapping("password")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates the password of a user", operationId = "updatePassword")
    public void updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                               @Valid @RequestBody PasswordChange passwordChange) {
        userDetailsRepository.updatePassword(userDetails.getUsername(), passwordChange.oldPassword(),
                passwordChange.newPassword());
    }
}
