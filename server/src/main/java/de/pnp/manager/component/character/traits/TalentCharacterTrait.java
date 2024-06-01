package de.pnp.manager.component.character.traits;

import de.pnp.manager.component.character.Talent;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Influences the talent.
 */
public class TalentCharacterTrait implements ICharacterTrait {

    @NotNull
    @DBRef
    private final Talent talent;

    private final int rollModifier;

    @NotNull
    private final String description;

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
        if (description.isBlank()) {
            return rollModifier + " " + talent.getName();
        }
        return description;
    }
}
