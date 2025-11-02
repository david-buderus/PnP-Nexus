package de.pnp.manager.component.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * A user on the server.
 *
 * @see PnPUserDetails
 */
public record PnPUser(@Id String username, @Indexed(unique = true) @NotNull @Size(min = 3) String displayName,
                      @Email String email) {
}
