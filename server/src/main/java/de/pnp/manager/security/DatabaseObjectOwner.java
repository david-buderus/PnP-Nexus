package de.pnp.manager.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static de.pnp.manager.security.SecurityConstants.DATABASE_OBJECT_TARGET_ID;
import static de.pnp.manager.security.SecurityConstants.OWNER;

/**
 * Method marked with this interface needs database object write rights.
 * <p>
 * Expects a path variable with the name "universe" and one with the name 'id'.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(DatabaseObjectOwner.AUTHORIZE_CONSTANT)
public @interface DatabaseObjectOwner {

    /**
     * The SpEL definition of this annotation
     */
    // language=SpEL prefix="@PreAuthorize('" suffix="')"
    String AUTHORIZE_CONSTANT = UniverseOwner.AUTHORIZE_CONSTANT + " || (" + UniverseRead.AUTHORIZE_CONSTANT + "&& hasPermission(#id, \"" + DATABASE_OBJECT_TARGET_ID + "\", \"" + OWNER + "\"))";
}
