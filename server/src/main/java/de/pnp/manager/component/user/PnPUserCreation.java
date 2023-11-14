package de.pnp.manager.component.user;

import de.pnp.manager.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

/**
 * DTO for user creation.
 */
public class PnPUserCreation {

    @NotBlank
    private final String username;

    @Password
    private final String password;

    @NotBlank
    private final String displayName;

    @Email
    private final String email;

    @NotNull
    private final Collection<? extends GrantedAuthority> authorities;

    public PnPUserCreation(String username, String password, String displayName, String email,
        Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.email = email;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
