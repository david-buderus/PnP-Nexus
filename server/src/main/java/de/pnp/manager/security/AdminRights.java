package de.pnp.manager.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static de.pnp.manager.security.SecurityConstants.ADMIN;

/**
 * Method marked with this interface needs admin rights.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(AdminRights.AUTHORIZE_CONSTANT)
public @interface AdminRights {

    /**
     * The SpEL definition of this annotation
     */
    // language=SpEL prefix="@PreAuthorize('" suffix="')"
    String AUTHORIZE_CONSTANT = "hasRole(\"" + ADMIN + "\")";
}
