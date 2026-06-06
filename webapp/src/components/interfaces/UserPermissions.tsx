import {GrantedDatabaseObjectIdAuthorityDTO, RoleAuthorityDTO, Universe} from '../../api/model';

/**
 * The permissions the currently authenticated user has on the active universe.
 */
export type UserPermissions = {
    /** User is admin */
    isAdmin: boolean;
    /** User can create universes */
    canCreateUniverses: boolean;
    /** User can read the currently selected universe */
    canReadActiveUniverse: boolean;
    /** User can write to the currently selected universe */
    canWriteActiveUniverse: boolean;
    /** User is the owner of the currently selected universe */
    isActiveUniverseOwner: boolean;
    /** All object permissions this user has */
    objectPermissions: Record<string, string>;
}

function extractUniversePermissions(universePermission: GrantedDatabaseObjectIdAuthorityDTO, activeUniverse: Universe, userPermissions: UserPermissions) {
    if (activeUniverse !== null && universePermission.id === activeUniverse.id) {
        switch (universePermission.permission) {
            case 'OWNER':
                userPermissions.isActiveUniverseOwner = true; // Fall through
            case 'WRITE':
                userPermissions.canWriteActiveUniverse = true; // Fall through
            case 'READ':
                userPermissions.canReadActiveUniverse = true;
                break;
            default:
                break;
        }
    }
}

function extractRolePermissions(rolePermission: RoleAuthorityDTO, userPermissions: UserPermissions) {
    switch (rolePermission.role) {
        case 'ADMIN':
            userPermissions.isActiveUniverseOwner = true;
            userPermissions.canWriteActiveUniverse = true;
            userPermissions.canReadActiveUniverse = true;
            userPermissions.isAdmin = true;
            userPermissions.canCreateUniverses = true;
            break;
        case 'UNIVERSE_CREATOR':
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
        isActiveUniverseOwner: false,
        objectPermissions: {}
    };

    for (const permission of permissions) {
        switch (permission['@type']) {
            case 'DatabaseObjectAuthority':
                const objectPermission = permission as GrantedDatabaseObjectIdAuthorityDTO;
                extractUniversePermissions(objectPermission, activeUniverse, userPermissions);
                userPermissions.objectPermissions[objectPermission.id] = objectPermission.permission;
                break;
            case 'Role':
                extractRolePermissions(permission as RoleAuthorityDTO, userPermissions);
                break;
            default:
                break;
        }
    }

    return userPermissions;
}

/**
 * If the user has edit rights for the given id.
 */
export function hasWriteRights(userPermissions: UserPermissions, id: string): boolean {
    if (userPermissions.isAdmin || userPermissions.isActiveUniverseOwner) {
        return true;
    }
    const permission = userPermissions.objectPermissions[id];
    if (!permission) {
        return false;
    }
    return permission === 'OWNER' || permission === 'WRITE';
}

/**
 * If the user has owner rights for the given id.
 */
export function hasOwnerRights(userPermissions: UserPermissions, id: string): boolean {
    if (userPermissions.isAdmin || userPermissions.isActiveUniverseOwner) {
        return true;
    }
    const permission = userPermissions.objectPermissions[id];
    if (!permission) {
        return false;
    }
    return permission === 'OWNER';
}