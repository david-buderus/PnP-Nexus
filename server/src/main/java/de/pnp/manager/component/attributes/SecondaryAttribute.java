package de.pnp.manager.component.attributes;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.character.PnPCharacter;
import de.pnp.manager.component.math.BinaryExpressionTree;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Objects;

/**
 * A secondary attribute of a {@link PnPCharacter}.
 */
public class SecondaryAttribute extends DatabaseObject implements IUniquelyNamedDataObject {

    /**
     * The human-readable name of this attribute.
     * <p>
     * This entry is always unique.
     */
    @Indexed(unique = true)
    @NotBlank
    private final String name;

    /**
     * The human-readable short name of this attribute.
     */
    @Indexed(unique = true)
    @NotBlank
    private final String shortName;

    @NotNull
    private final boolean consumable;

    @NotNull(message = "{expression.invalid}")
    private final BinaryExpressionTree calculationFormula;

    public SecondaryAttribute(ObjectId id, String name, String shortName, boolean consumable,
                              BinaryExpressionTree calculationFormula) {
        super(id);
        this.name = name;
        this.shortName = shortName;
        this.consumable = consumable;
        this.calculationFormula = calculationFormula;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public BinaryExpressionTree getCalculationFormula() {
        return calculationFormula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecondaryAttribute that = (SecondaryAttribute) o;
        return isConsumable() == that.isConsumable() && Objects.equals(getName(), that.getName())
                && Objects.equals(getShortName(), that.getShortName())
                && Objects.equals(getCalculationFormula(), that.getCalculationFormula());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getShortName(), isConsumable(), getCalculationFormula());
    }
}
