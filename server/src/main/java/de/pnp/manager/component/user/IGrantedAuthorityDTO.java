package de.pnp.manager.component.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.component.user.IGrantedAuthorityDTO.GrantedUniverseAuthorityDTO;
import de.pnp.manager.component.user.IGrantedAuthorityDTO.RoleAuthorityDTO;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * DTO for {@link GrantedAuthority}.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = RoleAuthorityDTO.class, name = "Role"),
    @JsonSubTypes.Type(value = GrantedUniverseAuthorityDTO.class, name = "UniverseAuthority"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface IGrantedAuthorityDTO {

    /**
     * Converts the DTO back to a {@link GrantedAuthority}.
     */
    GrantedAuthority convert();

    /**
     * Converts the given {@link GrantedAuthority} to an DTO.
     */
    static IGrantedAuthorityDTO from(GrantedAuthority authority) {
        if (authority instanceof SimpleGrantedAuthority && authority.getAuthority().startsWith("ROLE_")) {
            return new RoleAuthorityDTO(authority.getAuthority().substring("ROLE_".length()));
        }
        if (authority instanceof GrantedUniverseAuthority universeAuthority) {
            return new GrantedUniverseAuthorityDTO(universeAuthority.getUniverse(), universeAuthority.getAccessRight());
        }
        throw new UnsupportedOperationException("The authority " + authority.getClass().getSimpleName() +
            " with '" + authority.getAuthority() + "' is not supported.");
    }

    /**
     * Converts the given {@link GrantedAuthority} to an DTO.
     */
    static GrantedUniverseAuthorityDTO from(GrantedUniverseAuthority universeAuthority) {
        return new GrantedUniverseAuthorityDTO(universeAuthority.getUniverse(), universeAuthority.getAccessRight());
    }

    /**
     * Represents a role.
     */
    class RoleAuthorityDTO implements IGrantedAuthorityDTO {

        @NotBlank
        private final String role;

        public RoleAuthorityDTO(String role) {
            this.role = role;
        }

        @Override
        public GrantedAuthority convert() {
            return new SimpleGrantedAuthority("ROLE_" + getRole());
        }

        public String getRole() {
            return role;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RoleAuthorityDTO that = (RoleAuthorityDTO) o;
            return Objects.equals(getRole(), that.getRole());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getRole());
        }
    }

    /**
     * Represents {@link GrantedUniverseAuthority}
     */
    class GrantedUniverseAuthorityDTO implements IGrantedAuthorityDTO {

        @NotBlank
        private final String universe;

        @NotBlank
        private final String permission;

        public GrantedUniverseAuthorityDTO(String universe, String permission) {
            this.universe = universe;
            this.permission = permission;
        }

        @Override
        public GrantedAuthority convert() {
            return GrantedUniverseAuthority.fromPermission(getUniverse(), getPermission());
        }

        public String getUniverse() {
            return universe;
        }

        public String getPermission() {
            return permission;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            GrantedUniverseAuthorityDTO that = (GrantedUniverseAuthorityDTO) o;
            return Objects.equals(getUniverse(), that.getUniverse()) && Objects.equals(getPermission(),
                that.getPermission());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getUniverse(), getPermission());
        }
    }
}
