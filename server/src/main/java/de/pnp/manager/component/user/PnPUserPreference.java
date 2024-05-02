package de.pnp.manager.component.user;

import org.springframework.data.annotation.Id;

/**
 * The preferences of a user.
 *
 * @see PnPUser
 */
public record PnPUserPreference(@Id String username, String lastSelectedUniverse, String language) {

}
