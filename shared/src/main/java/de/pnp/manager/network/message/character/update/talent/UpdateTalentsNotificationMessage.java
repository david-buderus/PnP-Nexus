package de.pnp.manager.network.message.character.update.talent;

import de.pnp.manager.model.other.ITalent;

import java.util.Date;
import java.util.Map;

import static de.pnp.manager.network.message.MessageIDs.UPDATE_TALENTS_NOTIFICATION;

public class UpdateTalentsNotificationMessage extends UpdateTalentsMessage {

    public UpdateTalentsNotificationMessage() {
    }

    public UpdateTalentsNotificationMessage(String characterID, ITalent talent, int newValues, Date timestamp) {
        super(UPDATE_TALENTS_NOTIFICATION, characterID, talent, newValues, timestamp);
    }

    public UpdateTalentsNotificationMessage(String characterID, Map<ITalent, Integer> newValues, Date timestamp) {
        super(UPDATE_TALENTS_NOTIFICATION, characterID, newValues, timestamp);
    }
}