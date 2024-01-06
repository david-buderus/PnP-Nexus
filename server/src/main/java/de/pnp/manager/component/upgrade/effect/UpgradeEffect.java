package de.pnp.manager.component.upgrade.effect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.component.upgrade.Upgrade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Represents an effect of an {@link Upgrade}.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = SimpleUpgradeEffect.class, name = "SimpleUpgradeEffect"),
    @JsonSubTypes.Type(value = AdditiveUpgradeEffect.class, name = "AdditiveUpgradeEffect"),
    @JsonSubTypes.Type(value = MultiplicativeUpgradeEffect.class, name = "MultiplicativeUpgradeEffect"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public abstract class UpgradeEffect {

    /**
     * A human-readable description of the effect.
     */
    @NotBlank
    protected final String description;

    /**
     * Which value this {@link UpgradeEffect} manipulates.
     */
    @NotNull
    @JsonProperty
    protected final EUpgradeManipulator upgradeManipulator;

    public UpgradeEffect(String description, EUpgradeManipulator upgradeManipulator) {
        this.description = description;
        this.upgradeManipulator = upgradeManipulator;
    }

    /**
     * Applies the effect of this {@link UpgradeEffect}, if the given {@link EUpgradeManipulator} is compatible with
     * this upgrade.
     */
    public float apply(EUpgradeManipulator manipulator, float value) {
        if (manipulator == upgradeManipulator) {
            return apply(value);
        }
        return value;
    }

    /**
     * Applies the effect of this {@link UpgradeEffect}.
     */
    protected abstract float apply(float value);

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
        UpgradeEffect that = (UpgradeEffect) o;
        return Objects.equals(getDescription(), that.getDescription())
            && upgradeManipulator == that.upgradeManipulator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), upgradeManipulator);
    }
}
