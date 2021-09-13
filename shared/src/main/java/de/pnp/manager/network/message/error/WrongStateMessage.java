package de.pnp.manager.network.message.error;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.WRONG_STATE;

public class WrongStateMessage extends BaseMessage {

    public WrongStateMessage() {
    }

    public WrongStateMessage(Date timestamp) {
        super(WRONG_STATE, timestamp);
    }
}
