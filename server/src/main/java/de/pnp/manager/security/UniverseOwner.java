package de.pnp.manager.security;

import static de.pnp.manager.security.SecurityConstants.ADMIN;
import static de.pnp.manager.security.SecurityConstants.OWNER;
import static de.pnp.manager.security.SecurityConstants.UNIVERSE_TARGET_ID;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Method marked with this interface needs universe write rights.
 * <p>
 * Expects a path variable with the name "universe".
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(
    "hasRole('" + ADMIN + "') || hasPermission(#universe, '" + UNIVERSE_TARGET_ID + "', '" + OWNER + "')")
public @interface UniverseOwner {

}
