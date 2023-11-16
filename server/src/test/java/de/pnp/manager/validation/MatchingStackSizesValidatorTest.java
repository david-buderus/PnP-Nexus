package de.pnp.manager.validation;

import static de.pnp.manager.utils.TestItemBuilder.createItemBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for {@link MatchingStackSizesValidator}.
 */
class MatchingStackSizesValidatorTest {

    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            VALIDATOR = validatorFactory.getValidator();
        }
    }

    @ParameterizedTest
    @CsvSource({"0, 100", "1, 1", "10, 20", "1, 3"})
    void testValidStackSizes(int minStackSize, int maxStackSize) {
        Item item = createItemBuilder().withName("Test").withMinimumStackSize(minStackSize)
            .withMaximumStackSize(maxStackSize).buildItem();

        assertThat(VALIDATOR.validate(item)).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "10, 1", "100, 20", "13, 3"})
    void testInvalidStackSizes(int minStackSize, int maxStackSize) {
        Item item = createItemBuilder().withName("Test").withMinimumStackSize(minStackSize)
            .withMaximumStackSize(maxStackSize).buildItem();

        Set<ConstraintViolation<Item>> violations = VALIDATOR.validate(item);
        assertThat(violations).hasSize(1);
        ConstraintViolation<Item> violation = violations.stream().findFirst().orElseThrow();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("maximumStackSize");
    }
}