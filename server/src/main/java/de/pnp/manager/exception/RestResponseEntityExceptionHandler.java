package de.pnp.manager.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception Handler for all REST calls.
 */
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles {@link ValidationException}.
     */
    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationError(ValidationException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getNotValidatesFields(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
