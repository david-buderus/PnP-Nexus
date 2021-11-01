package de.pnp.manager.model.character;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PnPCharacterInfo {

    protected String characterID;
    protected String name;

    @JsonCreator
    public PnPCharacterInfo(
            @JsonProperty("characterID") String characterID,
            @JsonProperty("name") String name
    ) {
        this.characterID = characterID;
        this.name = name;
    }

    public String getCharacterID() {
        return characterID;
    }

    public void setCharacterID(String characterID) {
        this.characterID = characterID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
