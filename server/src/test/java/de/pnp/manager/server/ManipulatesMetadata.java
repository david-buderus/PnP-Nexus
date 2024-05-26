package de.pnp.manager.server;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;

/**
 * Marks tests which need to manipulate metadata, like users.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ResourceLock(value = "METADATA", mode = ResourceAccessMode.READ_WRITE)
public @interface ManipulatesMetadata {

}
