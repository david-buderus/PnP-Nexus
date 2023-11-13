package de.pnp.manager.component.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * Handles universe authorities.
 */
public class GrantedUniverseAuthority implements GrantedAuthority {

    public static GrantedUniverseAuthority readAuthority(String universe) {
        return new GrantedUniverseAuthority(universe, EAccessRight.READ);
    }

    public static GrantedUniverseAuthority writeAuthority(String universe) {
        return new GrantedUniverseAuthority(universe, EAccessRight.WRITE);
    }

    public static GrantedUniverseAuthority ownerAuthority(String universe) {
        return new GrantedUniverseAuthority(universe, EAccessRight.OWNER);
    }

    private final String universe;

    private final EAccessRight accessRight;

    private GrantedUniverseAuthority(String universe, EAccessRight accessRight) {
        this.universe = universe;
        this.accessRight = accessRight;
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
        if (accessRight == EAccessRight.READ) {
            return false;
        }
        return canRead(universe);
    }

    /**
     * Checks if this authority grants owner rights for the given universe.
     */
    public boolean isOwner(String universe) {
        return accessRight == EAccessRight.OWNER && canRead(universe);
    }

    @Override
    public String getAuthority() {
        return null;
    }

    private enum EAccessRight {
        READ, WRITE, OWNER
    }
}
