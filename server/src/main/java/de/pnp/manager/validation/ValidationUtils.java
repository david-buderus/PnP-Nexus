package de.pnp.manager.validation;

import org.bson.types.ObjectId;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Optional;

/**
 * Utils for validations
 */
public class ValidationUtils {

    private ValidationUtils() {
        // empty
    }

    /**
     * Returns the corresponding universe for the validation.
     */
    public static Optional<ObjectId> getUniverse() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return Optional.empty();
        }

        if (!(attributes.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
                RequestAttributes.SCOPE_REQUEST) instanceof Map<?, ?> pathVariables)) {
            return Optional.empty();
        }

        Object universePath = pathVariables.get("universe");
        if (universePath instanceof ObjectId s) {
            return Optional.of(s);
        }
        if (universePath instanceof String s && ObjectId.isValid(s)) {
            return Optional.of(new ObjectId(s));
        }

        return Optional.empty();
    }
}
