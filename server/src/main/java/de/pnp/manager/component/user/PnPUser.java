package de.pnp.manager.component.user;

import org.springframework.data.annotation.Id;

/**
 * A user on the server.
 *
 * @see PnPUserDetails
 */
public class PnPUser {

    @Id
    private final String username;
    private final String displayName;
    private final String email;

    public PnPUser(String username, String displayName, String email) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
