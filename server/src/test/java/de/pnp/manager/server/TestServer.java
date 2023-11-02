package de.pnp.manager.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Determines how the test server gets configured.
 *
 * @see ServerTestBase
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestServer {

    /**
     * The configuration used in the annotated test.
     */
    EServerTestConfiguration value();
}