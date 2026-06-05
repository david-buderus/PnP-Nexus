package de.pnp.manager.security;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IDTOWithId;
import de.pnp.manager.component.user.GrantedDatabaseObjectAuthority;
import org.bson.types.ObjectId;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * {@link PermissionEvaluator} for the server.
 */
public class PnPPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permissionObj) {
        if (targetDomainObject instanceof DatabaseObject databaseObject && permissionObj instanceof String permission) {
            return hasDatabaseObjectPrivilege(auth, databaseObject.getId(), permission);
        }
        if (targetDomainObject instanceof IDTOWithId dto && permissionObj instanceof String permission) {
            return hasDatabaseObjectPrivilege(auth, dto.id(), permission);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permissionObj) {
        if (SecurityConstants.UNIVERSE_TARGET_ID.equals(targetType) && targetId instanceof ObjectId id
                && permissionObj instanceof String permission) {
            return hasDatabaseObjectPrivilege(auth, id, permission);
        }
        if (SecurityConstants.DATABASE_OBJECT_TARGET_ID.equals(targetType) && targetId instanceof ObjectId id
                && permissionObj instanceof String permission) {
            return hasDatabaseObjectPrivilege(auth, id, permission);
        }
        if (SecurityConstants.DATABASE_OBJECT_TARGET_ID.equals(targetType) && targetId instanceof Collection<?> ids && permissionObj instanceof String permission) {
            for (Object rawId : ids) {
                if (!(rawId instanceof ObjectId id)) {
                    return false;
                }
                if (!hasDatabaseObjectPrivilege(auth, id, permission)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean hasDatabaseObjectPrivilege(Authentication auth, ObjectId universe, String permission) {
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority instanceof GrantedDatabaseObjectAuthority databaseObjectAuthority
                    && databaseObjectAuthority.hasRight(universe, permission)) {
                return true;
            }
        }
        return false;
    }
}
