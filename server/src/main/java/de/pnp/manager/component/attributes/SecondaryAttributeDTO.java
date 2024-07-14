package de.pnp.manager.component.attributes;

import de.pnp.manager.validation.IsValidExpression;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * This is a simpler format of {@link SecondaryAttribute}.
 *
 * @param id   The unique id of the {@link SecondaryAttribute}.
 * @param name The human-readable name of this attribute.
 */
public record SecondaryAttributeDTO(@Id ObjectId id, @Indexed(unique = true) @NotBlank String name,
                                    @NotNull boolean consumable,
                                    @NotNull @IsValidExpression String calculationFormula) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecondaryAttributeDTO that = (SecondaryAttributeDTO) o;
        return consumable() == that.consumable() && Objects.equals(name(), that.name())
            && Objects.equals(calculationFormula(), that.calculationFormula());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name(), consumable(), calculationFormula());
    }

    /**
     * Returns the matching {@link SecondaryAttributeDTO DTO} for the given {@link SecondaryAttribute}.
     */
    public static SecondaryAttributeDTO from(SecondaryAttribute attribute) {
        return new SecondaryAttributeDTO(attribute.getId(), attribute.getName(), attribute.isConsumable(),
            attribute.getCalculationFormula().asHumanReadableString());
    }
}
