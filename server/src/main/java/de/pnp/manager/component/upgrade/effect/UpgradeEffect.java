package de.pnp.manager.component.upgrade.effect;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.component.upgrade.Upgrade;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Represents an effect of an {@link Upgrade}.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = SimpleUpgradeEffect.class, name = "SimpleUpgradeEffect"),
    @JsonSubTypes.Type(value = EquipmentUpgradeEffect.class, name = "EquipmentUpgradeEffect")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public abstract class UpgradeEffect {

    /**
     * A human-readable description of the effect.
     */
    @NotBlank
    protected final String description;

    protected UpgradeEffect(String description) {
        this.description = description;
    }

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
        return Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription());
    }
}
