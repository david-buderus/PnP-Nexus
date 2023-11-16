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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {"some132Password$", "oap45d4as51!1325axXa", "üäöAD54§%adasdas"})
    void testValidPasswords(String password) {
        PnPUserCreation allowedCreation = new PnPUserCreation("user", password, "user", null, List.of());
        assertThat(VALIDATOR.validate(allowedCreation)).isEmpty();

        PasswordChange allowedChange = new PasswordChange("someText", password);
        assertThat(VALIDATOR.validate(allowedChange)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "1234", "myPassword", "myPassword123", "myPassword123123454846",
        "myPassword123öüaDA"})
    void testNotValidPasswords(String password) {
        PnPUserCreation disallowedCreation = new PnPUserCreation("user", password, "user", null, List.of());
        Set<ConstraintViolation<PnPUserCreation>> creationViolations = VALIDATOR.validate(disallowedCreation);
        assertThat(creationViolations).hasSize(1);
        ConstraintViolation<PnPUserCreation> creationViolation = creationViolations.stream().findFirst().orElseThrow();
        assertThat(creationViolation.getPropertyPath().toString()).isEqualTo("password");

        PasswordChange disallowedChange = new PasswordChange("someText", password);
        Set<ConstraintViolation<PasswordChange>> changeViolations = VALIDATOR.validate(disallowedChange);
        assertThat(changeViolations).hasSize(1);
        ConstraintViolation<PasswordChange> changeViolation = changeViolations.stream().findFirst().orElseThrow();
        assertThat(changeViolation.getPropertyPath().toString()).isEqualTo("newPassword");
    }
}
