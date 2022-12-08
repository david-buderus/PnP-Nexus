package de.pnp.manager.network.message.error;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.WRONG_STATE;

public class WrongStateMessage extends ErrorMessage<String> {

    public WrongStateMessage() {
    }

    public WrongStateMessage(String message, Date timestamp) {
        super(WRONG_STATE, timestamp);
        this.setData(message);
    }
}
