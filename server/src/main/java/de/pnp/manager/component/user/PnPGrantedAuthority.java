package de.pnp.manager.component.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * Handles universe authorities.
 */
public class PnPGrantedAuthority implements GrantedAuthority {

    private final String universe;

    private final boolean write;

    public PnPGrantedAuthority(String universe, boolean write) {
        this.universe = universe;
        this.write = write;
    }

    /**
     * Checks if this authority grants read rights for the given universe.
     */
    public boolean canRead(String universe) {
        return this.universe.equals(universe);
    }

    /**
     * Checks if this authority grants write rights for the given universe.
     */
    public boolean canWrite(String universe) {
        return write && canRead(universe);
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
