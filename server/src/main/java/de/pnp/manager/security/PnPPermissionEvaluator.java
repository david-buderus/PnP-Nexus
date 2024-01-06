package de.pnp.manager.security;

import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.component.user.GrantedUniverseAuthority;
import java.io.Serializable;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * {@link PermissionEvaluator} for the server.
 */
public class PnPPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permissionObj) {
        if (targetDomainObject instanceof Universe universe && permissionObj instanceof String permission) {
            return hasUniversePrivilege(auth, universe.getName(), permission);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permissionObj) {
        if (SecurityConstants.UNIVERSE_TARGET_ID.equals(targetType) && targetId instanceof String universe
            && permissionObj instanceof String permission) {
            return hasUniversePrivilege(auth, universe, permission);
        }
        return false;
    }

    private boolean hasUniversePrivilege(Authentication auth, String universe, String permission) {
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority instanceof GrantedUniverseAuthority universeAuthority && universeAuthority.hasRight(universe,
                permission)) {
                return true;
            }
        }
        return false;
    }
}
