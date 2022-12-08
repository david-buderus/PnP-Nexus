package de.pnp.manager.network.message.error;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.NOT_POSSIBLE;

public class NotPossibleMessage extends ErrorMessage<String> {

    public NotPossibleMessage() {
    }

    public NotPossibleMessage(String message, Date timestamp) {
        super(NOT_POSSIBLE, timestamp);
        this.setData(message);
    }
}
