package de.pnp.manager.component.character;

import org.bson.types.ObjectId;

import java.util.Map;

/**
 * The talents of a {@link PnPCharacter}
 */
public class CharacterTalents {

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
