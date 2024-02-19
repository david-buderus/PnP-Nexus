package de.pnp.manager.component.user;

import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * A user on the server.
 *
 * @see PnPUserDetails
 */
public class PnPUser {

    @Id
    private final String username;
    
    @Indexed(unique = true)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PnPUser pnPUser = (PnPUser) o;
        return getUsername().equals(pnPUser.getUsername()) && Objects.equals(getDisplayName(),
            pnPUser.getDisplayName()) && Objects.equals(getEmail(), pnPUser.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getDisplayName(), getEmail());
    }
}
