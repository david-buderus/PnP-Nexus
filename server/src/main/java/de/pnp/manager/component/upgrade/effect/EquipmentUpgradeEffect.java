package de.pnp.manager.component.upgrade.effect;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * An effect which directly changes the stats of the given equipment.
 */
public class EquipmentUpgradeEffect extends UpgradeEffect {

    /**
     * The value used for manipulation.
     */
    @NotNull
    private final float value;

    /**
     * Which value this {@link UpgradeEffect} manipulates.
     */
    @NotNull
    @JsonProperty
    private final EUpgradeEquipmentManipulator upgradeManipulator;

    /**
     * How the value gets manipulated.
     */
    @NotNull
    private final EUpgradeEffectCalculation calculation;

    public EquipmentUpgradeEffect(String description, float value, EUpgradeEquipmentManipulator upgradeManipulator,
        EUpgradeEffectCalculation calculation) {
        super(description);
        this.value = value;
        this.upgradeManipulator = upgradeManipulator;
        this.calculation = calculation;
    }

    /**
     * Applies the effect of this {@link UpgradeEffect}, if the given {@link EUpgradeEquipmentManipulator} is compatible
     * with this upgrade.
     */
    public float apply(EUpgradeEquipmentManipulator manipulator, float value) {
        if (manipulator != upgradeManipulator) {
            return value;
        }
        return switch (calculation) {
            case ADDITIVE -> value + this.value;
            case MULTIPLICATIVE -> value * this.value;
        };
    }

    public EUpgradeEffectCalculation getCalculation() {
        return calculation;
    }
}
