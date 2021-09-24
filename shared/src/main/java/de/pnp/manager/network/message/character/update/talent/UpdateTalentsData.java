package de.pnp.manager.network.message.character.update.talent;

import de.pnp.manager.model.other.ITalent;

import java.util.Map;

public class UpdateTalentsData {
    protected String characterID;
    protected Map<ITalent, Integer> newValues;

    public String getCharacterID() {
        return characterID;
    }

    public void setCharacterID(String characterID) {
        this.characterID = characterID;
    }

    public Map<ITalent, Integer> getNewValues() {
        return newValues;
    }

    public void setNewValues(Map<ITalent, Integer> newValues) {
        this.newValues = newValues;
    }
}
