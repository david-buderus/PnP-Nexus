package de.pnp.manager.component.user;

import de.pnp.manager.security.SecurityConstants;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static de.pnp.manager.security.SecurityConstants.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Handles universe authorities.
 */
public class GrantedUniverseAuthority implements GrantedAuthority {

    /**
     * Creates an {@link GrantedAuthority} which grants read access to the given universe.
     */
    public static GrantedUniverseAuthority readAuthority(ObjectId universe) {
        return new GrantedUniverseAuthority(universe, READ_ACCESS);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants write access to the given universe.
     */
    public static GrantedUniverseAuthority writeAuthority(ObjectId universe) {
        return new GrantedUniverseAuthority(universe, WRITE_ACCESS);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants owner access to the given universe.
     */
    public static GrantedUniverseAuthority ownerAuthority(ObjectId universe) {
        return new GrantedUniverseAuthority(universe, OWNER);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants the given permission to the given universe.
     */
    public static GrantedUniverseAuthority fromPermission(ObjectId universe, String accessPermission) {
        return switch (accessPermission) {
            case SecurityConstants.READ_ACCESS -> GrantedUniverseAuthority.readAuthority(universe);
            case SecurityConstants.WRITE_ACCESS -> GrantedUniverseAuthority.writeAuthority(universe);
            case SecurityConstants.OWNER -> GrantedUniverseAuthority.ownerAuthority(universe);
            default -> throw new ResponseStatusException(BAD_REQUEST,
                    "The access permission '" + accessPermission + "' is not supported.");
        };
    }

    private final ObjectId universe;

    private final String accessRight;

    private GrantedUniverseAuthority(ObjectId universe, String accessRight) {
        this.universe = universe;
        this.accessRight = accessRight;
    }

    /**
     * Checks if this authority grants read rights for the given universe.
     */
    public boolean canRead(ObjectId universe) {
        return this.universe.equals(universe);
    }

    /**
     * Checks if this authority grants write rights for the given universe.
     */
    public boolean canWrite(ObjectId universe) {
        if (READ_ACCESS.equals(accessRight)) {
            return false;
        }
        return canRead(universe);
    }

    /**
     * Checks if this authority grants owner rights for the given universe.
     */
    public boolean isOwner(ObjectId universe) {
        return OWNER.equals(accessRight) && canRead(universe);
    }

    /**
     * Checks if the authority grants the given rights for the given universe;
     */
    public boolean hasRight(ObjectId universe, String right) {
        return switch (right) {
            case READ_ACCESS -> canRead(universe);
            case WRITE_ACCESS -> canWrite(universe);
            case OWNER -> isOwner(universe);
            default -> false;
        };
    }

    public ObjectId getUniverse() {
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

    @Override
    public String toString() {
        return String.format("%s_ACCESS_%s", accessRight, universe);
    }
}
