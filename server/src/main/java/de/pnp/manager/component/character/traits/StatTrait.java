package de.pnp.manager.component.character.traits;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.stats.Stat;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Objects;

/**
 * Influences a {@link Stat}.
 */
public sealed abstract class StatTrait<Attribute extends IUniquelyNamedDataObject> implements ICharacterTrait {

    @NotNull
    @JsonProperty("calculation")
    private final ECalculation calculation;

    @JsonProperty("value")
    private final float value;

    @DBRef
    @NotNull
    @JsonProperty("attribute")
    private final Attribute attribute;

    @NotNull
    private final String description;

    protected StatTrait(ECalculation calculation, float value, Attribute attribute, String description) {
        this.calculation = calculation;
        this.value = value;
        this.attribute = attribute;
        this.description = description;
    }

    /**
     * Applies the trait if the attribute matches the attribute of the trait.
     */
    public float apply(Attribute attribute, float value) {
        if (Objects.equals(attribute, this.attribute)) {
            return switch (calculation) {
                case ADDITIVE -> value + this.value;
                case MULTIPLICATIVE -> value * this.value;
            };
        }
        return value;
    }

    @Override
    public String getDescription() {
        if (!description.isBlank()) {
            return description;
        }
        return switch (calculation) {
            case ADDITIVE -> {
                if (value > 0) {
                    yield "+" + value + " " + attribute.getName();
                }
                yield value + " " + attribute.getName();
            }
            case MULTIPLICATIVE -> value + " * " + attribute.getName();
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatTrait<?> statTrait = (StatTrait<?>) o;
        return Float.compare(statTrait.value, value) == 0 && calculation == statTrait.calculation
                && Objects.equals(attribute, statTrait.attribute) && Objects.equals(getDescription(),
                statTrait.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(calculation, value, attribute, getDescription());
    }

    /**
     * Influences {@link PrimaryAttribute}
     */
    public static final class PrimaryStatTrait extends StatTrait<PrimaryAttribute> {

        @JsonCreator
        public PrimaryStatTrait(ECalculation calculation, float value, PrimaryAttribute attribute,
                                String description) {
            super(calculation, value, attribute, description);
        }
    }

    /**
     * Influences {@link SecondaryAttribute}
     */
    public static final class SecondaryStatTrait extends StatTrait<SecondaryAttribute> {

        @JsonCreator
        public SecondaryStatTrait(ECalculation calculation, float value, SecondaryAttribute attribute,
                                  String description) {
            super(calculation, value, attribute, description);
        }
    }
}
