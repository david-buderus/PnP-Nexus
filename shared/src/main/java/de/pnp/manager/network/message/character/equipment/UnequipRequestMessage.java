package de.pnp.manager.network.message.character.equipment;

import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.UNEQUIP_REQUEST;

public class UnequipRequestMessage extends DataMessage<EquipmentData> {

    public UnequipRequestMessage() {
    }

    public UnequipRequestMessage(EquipmentData data, Date timestamp) {
        super(UNEQUIP_REQUEST, timestamp);
        this.setData(data);
    }
}
