package de.pnp.manager.component.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO to show which user has which access on a given universe.
 */
public record UserDatabaseObjectPermissionDTO(@NotBlank String displayName,
                                              @NotNull @Valid IGrantedAuthorityDTO.GrantedDatabaseObjectIdAuthorityDTO dto) {

}
