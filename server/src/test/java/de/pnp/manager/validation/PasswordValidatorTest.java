package de.pnp.manager.validation;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.server.service.UserService.PasswordChange;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PasswordValidatorTest}.
 */
public class PasswordValidatorTest {

    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            VALIDATOR = validatorFactory.getValidator();
        }
    }

    private static final String ALLOWED_PASSWORD = "some132Password$";
    private static final String DISALLOWED_PASSWORD = "myPassword";

    @Test
    void testUserCreation() {
        PnPUserCreation allowedCreation = new PnPUserCreation("user", ALLOWED_PASSWORD, "user", null, List.of());
        assertThat(VALIDATOR.validate(allowedCreation)).isEmpty();

        PnPUserCreation disallowedCreation = new PnPUserCreation("user", DISALLOWED_PASSWORD, "user", null, List.of());
        Set<ConstraintViolation<PnPUserCreation>> violations = VALIDATOR.validate(disallowedCreation);
        assertThat(violations).hasSize(1);
        ConstraintViolation<PnPUserCreation> violation = violations.stream().findFirst().orElseThrow();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
    }

    @Test
    void testPasswordChange() {
        PasswordChange allowedChange = new PasswordChange("someText", ALLOWED_PASSWORD);
        assertThat(VALIDATOR.validate(allowedChange)).isEmpty();

        PasswordChange disallowedChange = new PasswordChange("someText", DISALLOWED_PASSWORD);
        Set<ConstraintViolation<PasswordChange>> violations = VALIDATOR.validate(disallowedChange);
        assertThat(violations).hasSize(1);
        ConstraintViolation<PasswordChange> violation = violations.stream().findFirst().orElseThrow();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("newPassword");
    }
}
