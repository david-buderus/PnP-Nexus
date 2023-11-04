package de.pnp.manager.security;

import de.pnp.manager.component.user.PnPGrantedAuthority;
import java.io.Serializable;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * {@link PermissionEvaluator} for the server.
 */
public class PnPPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permissionObj) {
        if ((auth == null) || !(targetId instanceof String universe) || !(permissionObj instanceof String permission)) {
            return false;
        }
        if ("UNIVERSE".equals(targetType)) {
            return hasUniversePrivilege(auth, permission, universe);
        }
        return false;
    }

    private boolean hasUniversePrivilege(Authentication auth, String permission, String universe) {
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (!(authority instanceof PnPGrantedAuthority universeAuthority)) {
                continue;
            }
            switch (permission) {
                case "READ":
                    if (universeAuthority.canRead(universe)) {
                        return true;
                    }
                    break;
                case "WRITE":
                    if (universeAuthority.canWrite(universe)) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}
