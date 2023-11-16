package de.pnp.manager.server.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Test utils for service tests.
 */
public abstract class ServiceTestUtils {

    /**
     * Asserts that the callable throws a {@link ResponseStatusException} with the status {@link HttpStatus#FORBIDDEN}.
     */
    public static void assertForbidden(ThrowingCallable callable) {
        assertHttpStatusException(callable, HttpStatus.FORBIDDEN);
    }

    /**
     * Asserts that the callable throws a {@link ResponseStatusException} with the given {@link HttpStatus}.
     */
    public static void assertHttpStatusException(ThrowingCallable callable, HttpStatus status) {
        assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(callable)
            .extracting(ResponseStatusException::getStatusCode).isEqualTo(status);
    }
}
