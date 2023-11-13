package de.pnp.manager.component.user;

import static de.pnp.manager.security.SecurityConstants.OWNER;
import static de.pnp.manager.security.SecurityConstants.READ_ACCESS;
import static de.pnp.manager.security.SecurityConstants.WRITE_ACCESS;

import org.springframework.security.core.GrantedAuthority;

/**
 * Handles universe authorities.
 */
public class GrantedUniverseAuthority implements GrantedAuthority {

    /**
     * Creates an {@link GrantedAuthority} which grants read access to the given universe.
     */
    public static GrantedUniverseAuthority readAuthority(String universe) {
        return new GrantedUniverseAuthority(universe, EAccessRight.READ);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants write access to the given universe.
     */
    public static GrantedUniverseAuthority writeAuthority(String universe) {
        return new GrantedUniverseAuthority(universe, EAccessRight.WRITE);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants owner access to the given universe.
     */
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

    /**
     * Checks if the authority grants the given rights for the given universe;
     */
    public boolean hasRight(String universe, String right) {
        return switch (right) {
            case READ_ACCESS -> canRead(universe);
            case WRITE_ACCESS -> canWrite(universe);
            case OWNER -> isOwner(universe);
            default -> false;
        };
    }

    @Override
    public String getAuthority() {
        return null;
    }

    private enum EAccessRight {
        READ, WRITE, OWNER
    }
}
