package de.pnp.manager.component.user;

import de.pnp.manager.validation.Password;
import jakarta.validation.Valid;
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
    private final Collection<@Valid IGrantedAuthorityDTO> authorities;

    public PnPUserCreation(String username, String password, String displayName, String email,
        Collection<IGrantedAuthorityDTO> authorities) {
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

    public Collection<IGrantedAuthorityDTO> getAuthorities() {
        return authorities;
    }

    /**
     * Creates a simple user creation object.
     * <p>
     * Should only be used in DEV and tests.
     */
    public static PnPUserCreation simple(String username, Collection<GrantedAuthority> authorities) {
        return new PnPUserCreation(username, username, username, null,
            authorities.stream().map(IGrantedAuthorityDTO::from).toList());
    }
}
