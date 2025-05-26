package de.pnp.manager.component.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * The talents of a {@link PnPCharacter}
 */
public class CharacterTalents {

    @NotNull
    @JsonProperty("talents")
    private final Map<ObjectId, Integer> talents;

    public CharacterTalents(Map<ObjectId, Integer> talents) {
        this.talents = talents;
    }

    public int getRoll(Talent talent) {
        return talents.getOrDefault(talent.getId(), 0);
    }

    public void setRoll(Talent talent, int roll) {
        talents.put(talent.getId(), roll);
    }
}
