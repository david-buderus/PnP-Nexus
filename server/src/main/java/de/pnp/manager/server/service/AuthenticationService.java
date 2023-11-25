package de.pnp.manager.server.service;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to authenticate.
 */
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationService {

    @GetMapping("current-user")
    @Operation(summary = "Returns the username of the authenticated user", operationId = "getUsername")
    public String getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getUsername();
    }
}
