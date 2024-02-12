package de.pnp.manager.component.user;

import de.pnp.manager.component.user.IGrantedAuthorityDTO.GrantedUniverseAuthorityDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO to show which user has which access on a given universe.
 */
public record UserUniversePermissionDTO(@NotBlank String displayName, @NotNull @Valid GrantedUniverseAuthorityDTO dto) {

}
