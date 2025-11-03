package de.pnp.manager.component.character.traits;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.pnp.manager.component.character.Talent;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Objects;

/**
 * Influences the talent.
 */
public class TalentCharacterTrait implements ICharacterTrait {

    @DBRef
    @NotNull
    @JsonProperty("talent")
    private final Talent talent;

    @NotNull
    @JsonProperty("rollModifier")
    private final int rollModifier;

    @NotNull
    private final String description;

    @JsonCreator
    public TalentCharacterTrait(Talent talent, int rollModifier, String description) {
        this.talent = talent;
        this.rollModifier = rollModifier;
        this.description = description;
    }

    /**
     * Applies the trait on the talent.
     */
    public int apply(Talent talent, int value) {
        if (Objects.equals(talent, this.talent)) {
            return value + rollModifier;
        }
        return value;
    }

    @Override
    public String getDescription() {
        if (!description.isBlank()) {
            return description;
        }
        if (rollModifier > 0) {
            return "+" + rollModifier + " " + talent.getName();
        }
        return rollModifier + " " + talent.getName();
    }
}
