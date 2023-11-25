import { GrantedUniverseAuthorityDTO, RoleAuthorityDTO, Universe } from "../../api";

/**
 * The permissions the currently authenticated user has on the active universe.
 */
export interface UserPermissions {
  isAdmin: boolean;
  canCreateUniverses: boolean;
  canReadActiveUniverse: boolean;
  canWriteActiveUniverse: boolean;
  isActiveUniverseOwner: boolean;
}

/**
 * Extracts {@link UserPermissions} from the server DTOs.
 */
export function extractUserPermissions(permissions: any[], activeUniverse: Universe): UserPermissions {
  const userPermissions: UserPermissions = {
    isAdmin: false,
    canCreateUniverses: false,
    canReadActiveUniverse: false,
    canWriteActiveUniverse: false,
    isActiveUniverseOwner: false
  }

  for (const permission of permissions) {
    switch (permission["@type"]) {
      case "UniverseAuthority":
        const universePermission = permission as GrantedUniverseAuthorityDTO;
        if (activeUniverse !== null && universePermission.universe === activeUniverse.name) {
          switch (universePermission.permission) {
            case "OWNER": userPermissions.isActiveUniverseOwner = true;
            case "WRITE": userPermissions.canWriteActiveUniverse = true;
            case "READ": userPermissions.canReadActiveUniverse = true;
          }
        }
        break;
      case "Role":
        const rolePermission = permission as RoleAuthorityDTO;
        switch (rolePermission.role) {
          case "ADMIN": userPermissions.isActiveUniverseOwner = true;
            userPermissions.canWriteActiveUniverse = true;
            userPermissions.canReadActiveUniverse = true;
            userPermissions.isAdmin = true;
            userPermissions.canCreateUniverses = true;
            break;
          case "UNIVERSE_CREATOR":
            userPermissions.canCreateUniverses = true;
            break;
        }
        break;
    }
  }

  return userPermissions;
}