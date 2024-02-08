package de.pnp.manager.exception;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoServerException;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteError;
import com.mongodb.bulk.BulkWriteError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
     * Handles {@link ConstraintViolationException}.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
        WebRequest request) {
        Map<String, String> response = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            response.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles {@link MongoWriteException} and {@link MongoBulkWriteException}.
     */
    @ExceptionHandler({MongoWriteException.class, MongoBulkWriteException.class})
    protected ResponseEntity<Object> handleMongoWriteException(MongoServerException ex,
        WebRequest request) {
        if (ex instanceof MongoWriteException writeException) {
            return handleDuplicateKeys(writeException, request, List.of(writeException.getError()));
        }
        if (ex instanceof MongoBulkWriteException writeException) {
            return handleDuplicateKeys(writeException, request, writeException.getWriteErrors());
        }

        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> handleDuplicateKeys(MongoServerException ex, WebRequest request,
        List<? extends WriteError> writeErrors) {
        if (writeErrors.stream().anyMatch(error -> error.getCategory() != ErrorCategory.DUPLICATE_KEY)) {
            return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
        ResourceBundle bundle = ResourceBundle.getBundle("messages", request.getLocale());
        String errorMessage = bundle.getString("error.duplicate.key");

        Map<String, String> response = new HashMap<>();

        for (WriteError writeError : writeErrors) {
            String field = extractWriteErrorField(writeError);
            if (writeError instanceof BulkWriteError bulkWriteError) {
                response.put("objects[" + bulkWriteError.getIndex() + "]." + field, errorMessage);
            } else {
                response.put(field, errorMessage);
            }
        }

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private String extractWriteErrorField(WriteError error) {
        String message = error.getMessage();
        int startIndex = message.indexOf("index:") + 6;
        int endIndex = message.indexOf("dup key:");
        return message.substring(startIndex, endIndex).trim();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex,
        @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        Map<String, String> response = new HashMap<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            response.put(fieldName, errorMessage);
        }

        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }
}
