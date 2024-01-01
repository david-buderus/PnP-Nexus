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

function extractUniversePermissions(universePermission: GrantedUniverseAuthorityDTO, activeUniverse: Universe, userPermissions: UserPermissions) {
  if (activeUniverse !== null && universePermission.universe === activeUniverse.name) {
    switch (universePermission.permission) {
      case "OWNER": userPermissions.isActiveUniverseOwner = true; // Fall through
      case "WRITE": userPermissions.canWriteActiveUniverse = true; // Fall through
      case "READ": userPermissions.canReadActiveUniverse = true;
        break;
      default:
        break;
    }
  }
}

function extractRolePermissions(rolePermission: RoleAuthorityDTO, userPermissions: UserPermissions) {
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
    default:
      break;
  }
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
        extractUniversePermissions(permission as GrantedUniverseAuthorityDTO, activeUniverse, userPermissions);
        break;
      case "Role":
        const rolePermission = permission as RoleAuthorityDTO;
        extractRolePermissions(rolePermission, userPermissions);
        break;
      default:
        break;
    }
  }

  return userPermissions;
}
