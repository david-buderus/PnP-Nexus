package de.pnp.manager.network.message.character.equipment;

import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.EQUIP_REQUEST;

public class EquipRequestMessage extends DataMessage<EquipmentData> {

    public EquipRequestMessage() {
    }

    public EquipRequestMessage(EquipmentData data, Date timestamp) {
        super(EQUIP_REQUEST, timestamp);
        this.setData(data);
    }
}
