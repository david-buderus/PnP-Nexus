package de.pnp.manager.validation;

import static de.pnp.manager.server.database.TestItemBuilder.createItemBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MatchingItemTypesValidator}.
 */
class MatchingItemTypesValidatorTest {

    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            VALIDATOR = validatorFactory.getValidator();
        }
    }

    @Test
    void testTypeValidation() {
        ItemType wrongType = new ItemType(null, "Weapon type", ETypeRestriction.HANDHELD);
        Item item = createItemBuilder().withName("Test").withType(wrongType).buildItem();

        Set<ConstraintViolation<Item>> violations = VALIDATOR.validate(item);
        assertThat(violations).hasSize(1);
        ConstraintViolation<Item> violation = violations.stream().findFirst().orElseThrow();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("type");
    }

    @Test
    void testSubtypeValidation() {
        ItemType wrongType = new ItemType(null, "Armor type", ETypeRestriction.ARMOR);
        Item item = createItemBuilder().withName("Test").withSubtype(wrongType).buildItem();

        Set<ConstraintViolation<Item>> violations = VALIDATOR.validate(item);
        assertThat(violations).hasSize(1);
        ConstraintViolation<Item> violation = violations.stream().findFirst().orElseThrow();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("subtype");
    }

    @Test
    void testBothTypeValidation() {
        ItemType wrongType = new ItemType(null, "Jewellery type", ETypeRestriction.JEWELLERY);
        Item item = createItemBuilder().withName("Test").withType(wrongType).withSubtype(wrongType).buildItem();

        Set<ConstraintViolation<Item>> violations = VALIDATOR.validate(item);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
            .containsExactlyInAnyOrder("type", "subtype");
    }
}