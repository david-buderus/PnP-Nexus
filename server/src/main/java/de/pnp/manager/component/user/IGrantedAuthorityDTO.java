package de.pnp.manager.component.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.component.user.IGrantedAuthorityDTO.GrantedDatabaseObjectIdAuthorityDTO;
import de.pnp.manager.component.user.IGrantedAuthorityDTO.RoleAuthorityDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Objects;

/**
 * DTO for {@link GrantedAuthority}.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoleAuthorityDTO.class, name = "Role"),
        @JsonSubTypes.Type(value = GrantedDatabaseObjectIdAuthorityDTO.class, name = "DatabaseObjectAuthority"),
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
        if (authority instanceof GrantedDatabaseObjectAuthority databaseObjectAuthority) {
            return new GrantedDatabaseObjectIdAuthorityDTO(databaseObjectAuthority.getObjectId(), databaseObjectAuthority.getAccessRight());
        }
        throw new UnsupportedOperationException("The authority " + authority.getClass().getSimpleName() +
                " with '" + authority.getAuthority() + "' is not supported.");
    }

    /**
     * Converts the given {@link GrantedAuthority} to an DTO.
     */
    static GrantedDatabaseObjectIdAuthorityDTO from(GrantedDatabaseObjectAuthority databaseObjectAuthority) {
        return new GrantedDatabaseObjectIdAuthorityDTO(databaseObjectAuthority.getObjectId(), databaseObjectAuthority.getAccessRight());
    }

    /**
     * Represents a role.
     */
    class RoleAuthorityDTO implements IGrantedAuthorityDTO {

        @NotBlank
        private final String role;

        @JsonCreator
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
     * Represents {@link GrantedDatabaseObjectAuthority}
     */
    class GrantedDatabaseObjectIdAuthorityDTO implements IGrantedAuthorityDTO {

        @NotNull
        private final ObjectId id;

        @NotBlank
        private final String permission;

        @JsonCreator
        public GrantedDatabaseObjectIdAuthorityDTO(ObjectId id, String permission) {
            this.id = id;
            this.permission = permission;
        }

        @Override
        public GrantedAuthority convert() {
            return GrantedDatabaseObjectAuthority.fromPermission(getId(), getPermission());
        }

        public ObjectId getId() {
            return id;
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
            GrantedDatabaseObjectIdAuthorityDTO that = (GrantedDatabaseObjectIdAuthorityDTO) o;
            return Objects.equals(getId(), that.getId()) && Objects.equals(getPermission(),
                    that.getPermission());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getPermission());
        }
    }
}
