package de.pnp.manager.network.message.character;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.CONTROLLED_CHARACTER_REQUEST;

public class ControlledCharacterRequestMessage extends BaseMessage {

    public ControlledCharacterRequestMessage() {
    }

    public ControlledCharacterRequestMessage(Date timestamp) {
        super(CONTROLLED_CHARACTER_REQUEST, timestamp);
    }
}
