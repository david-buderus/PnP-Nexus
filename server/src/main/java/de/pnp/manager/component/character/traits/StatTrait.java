package de.pnp.manager.component.character.traits;


import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.stats.Stat;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Influences a {@link Stat}.
 */
public sealed abstract class StatTrait<Attribute> implements ICharacterTrait {

    @NotNull
    private final ECalculation calculation;

    private final float value;

    @DBRef
    @NotNull
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
        return description;
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

        public PrimaryStatTrait(ECalculation calculation, float value, PrimaryAttribute primaryAttribute,
            String description) {
            super(calculation, value, primaryAttribute, description);
        }
    }

    /**
     * Influences {@link SecondaryAttribute}
     */
    public static final class SecondaryStatTrait extends StatTrait<SecondaryAttribute> {

        public SecondaryStatTrait(ECalculation calculation, float value, SecondaryAttribute secondaryAttribute,
            String description) {
            super(calculation, value, secondaryAttribute, description);
        }
    }
}
