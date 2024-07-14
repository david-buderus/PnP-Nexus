package de.pnp.manager.server.service;

import de.pnp.manager.ServerApplication;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface to indicate that the {@link Operation#operationId() operation id} gets rewritten.
 *
 * @see ServerApplication#operationIdCustomizer()
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RewriteOperationId {

}
