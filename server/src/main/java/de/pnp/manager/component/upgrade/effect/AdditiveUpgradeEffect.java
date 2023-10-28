package de.pnp.manager.component.upgrade.effect;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * An {@link UpgradeEffect} which adds a value to an attribute.
 */
public class AdditiveUpgradeEffect extends UpgradeEffect {

    /**
     * The value which gets added to the corresponding attribute.
     */
    @JsonProperty
    private final float value;

    public AdditiveUpgradeEffect(String description, EUpgradeManipulator upgradeManipulator, float value) {
        super(description, upgradeManipulator);
        this.value = value;
    }

    @Override
    protected float apply(float value) {
        return value + this.value;
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
        AdditiveUpgradeEffect that = (AdditiveUpgradeEffect) o;
        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
