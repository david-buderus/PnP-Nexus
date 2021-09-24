package de.pnp.manager.network.message.character.update.talent;

import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public abstract class UpdateTalentsMessage extends DataMessage<UpdateTalentsData> {

    protected UpdateTalentsMessage() {
    }

    protected UpdateTalentsMessage(int id, String characterID, ITalent talent, int newValues, Date timestamp) {
        this(id, characterID, Collections.singletonMap(talent, newValues), timestamp);
    }

    protected UpdateTalentsMessage(int id, String characterID, Map<ITalent, Integer> newValues, Date timestamp) {
        super(id, timestamp);
        UpdateTalentsData data = new UpdateTalentsData();
        data.setCharacterID(characterID);
        data.setNewValues(newValues);
        this.setData(data);
    }
}
