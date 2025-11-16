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
public class GrantedDatabaseObjectAuthority implements GrantedAuthority {

    /**
     * Creates an {@link GrantedAuthority} which grants read access to the given database object.
     */
    public static GrantedDatabaseObjectAuthority readAuthority(ObjectId id) {
        return new GrantedDatabaseObjectAuthority(id, READ_ACCESS);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants write access to the given database object.
     */
    public static GrantedDatabaseObjectAuthority writeAuthority(ObjectId id) {
        return new GrantedDatabaseObjectAuthority(id, WRITE_ACCESS);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants owner access to the given database object.
     */
    public static GrantedDatabaseObjectAuthority ownerAuthority(ObjectId id) {
        return new GrantedDatabaseObjectAuthority(id, OWNER);
    }

    /**
     * Creates an {@link GrantedAuthority} which grants the given permission to the given database object.
     */
    public static GrantedDatabaseObjectAuthority fromPermission(ObjectId id, String accessPermission) {
        return switch (accessPermission) {
            case SecurityConstants.READ_ACCESS -> GrantedDatabaseObjectAuthority.readAuthority(id);
            case SecurityConstants.WRITE_ACCESS -> GrantedDatabaseObjectAuthority.writeAuthority(id);
            case SecurityConstants.OWNER -> GrantedDatabaseObjectAuthority.ownerAuthority(id);
            default -> throw new ResponseStatusException(BAD_REQUEST,
                    "The access permission '" + accessPermission + "' is not supported.");
        };
    }

    private final ObjectId objectId;

    private final String accessRight;

    private GrantedDatabaseObjectAuthority(ObjectId objectId, String accessRight) {
        this.objectId = objectId;
        this.accessRight = accessRight;
    }

    /**
     * Checks if this authority grants read rights for the given universe.
     */
    public boolean canRead(ObjectId id) {
        return this.objectId.equals(id);
    }

    /**
     * Checks if this authority grants write rights for the given universe.
     */
    public boolean canWrite(ObjectId id) {
        if (READ_ACCESS.equals(accessRight)) {
            return false;
        }
        return canRead(id);
    }

    /**
     * Checks if this authority grants owner rights for the given universe.
     */
    public boolean isOwner(ObjectId id) {
        return OWNER.equals(accessRight) && canRead(id);
    }

    /**
     * Checks if the authority grants the given rights for the given universe;
     */
    public boolean hasRight(ObjectId id, String right) {
        return switch (right) {
            case READ_ACCESS -> canRead(id);
            case WRITE_ACCESS -> canWrite(id);
            case OWNER -> isOwner(id);
            default -> false;
        };
    }

    public ObjectId getObjectId() {
        return objectId;
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
        GrantedDatabaseObjectAuthority that = (GrantedDatabaseObjectAuthority) o;
        return getObjectId().equals(that.getObjectId()) && Objects.equals(accessRight, that.accessRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getObjectId(), accessRight);
    }

    @Override
    public String getAuthority() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s_ACCESS_%s", accessRight, objectId);
    }
}
