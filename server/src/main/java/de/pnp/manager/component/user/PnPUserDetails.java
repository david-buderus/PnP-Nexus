package de.pnp.manager.component.user;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Authentication details of a user.
 */
public class PnPUserDetails implements UserDetails {

    @Id
    private final String username;
    private final String password;
    private final Collection<PnPGrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final boolean admin;

    public PnPUserDetails(String username, String password, Collection<PnPGrantedAuthority> authorities,
        boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled,
        boolean admin) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.admin = admin;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (!isAdmin()) {
            return authorities;
        }
        Collection<GrantedAuthority> adminAuthorities = new ArrayList<>(authorities);
        adminAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return adminAuthorities;
    }

    public Collection<PnPGrantedAuthority> getPnPAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAdmin() {
        return admin;
    }
}
