package de.pnp.manager.component.character;

import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * The talents of a {@link PnPCharacter}
 */
public class CharacterTalents {

    @NotEmpty
    private final Map<Talent, Integer> talents;

    public CharacterTalents(Map<Talent, Integer> talents) {
        this.talents = talents;
    }

    public int getRoll(Talent talent) {
        return talents.getOrDefault(talent, 0);
    }

    public void setRoll(Talent talent, int roll) {
        talents.put(talent, roll);
    }
}
