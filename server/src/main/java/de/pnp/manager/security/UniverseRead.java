package de.pnp.manager.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static de.pnp.manager.security.SecurityConstants.READ_ACCESS;
import static de.pnp.manager.security.SecurityConstants.UNIVERSE_TARGET_ID;

/**
 * Method marked with this interface needs universe read rights.
 * <p>
 * Expects a path variable with the name "universe".
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(UniverseRead.AUTHORIZE_CONSTANT)
public @interface UniverseRead {

    /**
     * The SpEL definition of this annotation
     */
    // language=SpEL prefix="@PreAuthorize('" suffix="')"
    String AUTHORIZE_CONSTANT = AdminRights.AUTHORIZE_CONSTANT + " || hasPermission(#universe, \"" + UNIVERSE_TARGET_ID + "\", \"" + READ_ACCESS + "\")";
}
