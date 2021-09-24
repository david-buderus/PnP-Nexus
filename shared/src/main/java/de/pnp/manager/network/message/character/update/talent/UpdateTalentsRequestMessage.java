package de.pnp.manager.network.message.character.update.talent;

import de.pnp.manager.model.other.ITalent;

import java.util.Date;
import java.util.Map;

import static de.pnp.manager.network.message.MessageIDs.UPDATE_TALENTS_REQUEST;

public class UpdateTalentsRequestMessage extends UpdateTalentsMessage {

    public UpdateTalentsRequestMessage() {
    }

    public UpdateTalentsRequestMessage(String characterID, ITalent talent, int newValues, Date timestamp) {
        super(UPDATE_TALENTS_REQUEST, characterID, talent, newValues, timestamp);
    }

    public UpdateTalentsRequestMessage(String characterID, Map<ITalent, Integer> newValues, Date timestamp) {
        super(UPDATE_TALENTS_REQUEST, characterID, newValues, timestamp);
    }
}
