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
        @JsonSubTypes.Type(value = SimpleItemEffect.class, name = "SimpleItemEffect"),
        @JsonSubTypes.Type(value = EquipmentItemEffect.class, name = "EquipmentItemEffect")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public abstract class ItemEffect {

    /**
     * A human-readable description of the effect.
     */
    @NotBlank
    protected final String description;

    protected ItemEffect(String description) {
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
        ItemEffect that = (ItemEffect) o;
        return Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription());
    }
}
