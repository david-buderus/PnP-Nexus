package de.pnp.manager.component.user;

import static de.pnp.manager.security.SecurityConstants.OWNER;
import static de.pnp.manager.security.SecurityConstants.READ_ACCESS;
import static de.pnp.manager.security.SecurityConstants.WRITE_ACCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import de.pnp.manager.security.SecurityConstants;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

/**
 * Handles universe authorities.
 */
public class GrantedUniverseAuthority implements GrantedAuthority {

    /**
     * Creates an {@link GrantedAuthority} which grants read access to the given universe.
     */
    public static GrantedUniverseAuthority readAuthority(String universe) {
        return new GrantedUniverseAuthority(universe, READ_ACCESS);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants write access to the given universe.
     */
    public static GrantedUniverseAuthority writeAuthority(String universe) {
        return new GrantedUniverseAuthority(universe, WRITE_ACCESS);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants owner access to the given universe.
     */
    public static GrantedUniverseAuthority ownerAuthority(String universe) {
        return new GrantedUniverseAuthority(universe, OWNER);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants the given permission to the given universe.
     */
    public static GrantedUniverseAuthority fromPermission(String universe, String accessPermission) {
        return switch (accessPermission) {
            case SecurityConstants.READ_ACCESS -> GrantedUniverseAuthority.readAuthority(universe);
            case SecurityConstants.WRITE_ACCESS -> GrantedUniverseAuthority.writeAuthority(universe);
            case SecurityConstants.OWNER -> GrantedUniverseAuthority.ownerAuthority(universe);
            default -> throw new ResponseStatusException(BAD_REQUEST,
                "The access permission '" + accessPermission + "' is not supported.");
        };
    }

    private final String universe;

    private final String accessRight;

    private GrantedUniverseAuthority(String universe, String accessRight) {
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
        if (READ_ACCESS.equals(accessRight)) {
            return false;
        }
        return canRead(universe);
    }

    /**
     * Checks if this authority grants owner rights for the given universe.
     */
    public boolean isOwner(String universe) {
        return OWNER.equals(accessRight) && canRead(universe);
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

    public String getUniverse() {
        return universe;
    }

    public String getAccessRight() {
        return accessRight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GrantedUniverseAuthority that = (GrantedUniverseAuthority) o;
        return getUniverse().equals(that.getUniverse()) && Objects.equals(accessRight, that.accessRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniverse(), accessRight);
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
