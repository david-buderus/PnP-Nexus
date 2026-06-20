package de.pnp.manager.component.upgrade.effect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import de.pnp.manager.component.ECalculation;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * An effect which directly changes the stats of the given equipment.
 */
public class EquipmentItemEffect extends ItemEffect {

    /**
     * The value used for manipulation.
     */
    @NotNull
    @JsonProperty
    private final float value;

    /**
     * Which value this {@link ItemEffect} manipulates.
     */
    @NotNull
    @JsonProperty
    private final EItemEquipmentManipulator upgradeManipulator;

    /**
     * How the value gets manipulated.
     */
    @NotNull
    @JsonProperty
    private final ECalculation calculation;

    public EquipmentItemEffect(String description, float value, EItemEquipmentManipulator upgradeManipulator,
                               ECalculation calculation) {
        super(description);
        this.value = value;
        this.upgradeManipulator = upgradeManipulator;
        this.calculation = calculation;
    }

    /**
     * Applies the effect of this {@link ItemEffect}, if the given {@link EItemEquipmentManipulator} is compatible
     * with this upgrade.
     */
    public float apply(EItemEquipmentManipulator manipulator, float value) {
        if (manipulator != upgradeManipulator) {
            return value;
        }
        return switch (calculation) {
            case ADDITIVE -> value + this.value;
            case MULTIPLICATIVE -> value * this.value;
        };
    }

    public ECalculation getCalculation() {
        return calculation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        EquipmentItemEffect that = (EquipmentItemEffect) o;
        return Float.compare(that.value, value) == 0 && upgradeManipulator == that.upgradeManipulator
                && getCalculation() == that.getCalculation();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, upgradeManipulator, getCalculation());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .add("upgradeManipulator", upgradeManipulator)
                .add("calculation", calculation)
                .add("description", description)
                .toString();
    }
}
