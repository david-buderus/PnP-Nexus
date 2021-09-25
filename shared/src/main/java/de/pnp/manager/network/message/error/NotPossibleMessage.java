package de.pnp.manager.network.message.error;

import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.NOT_POSSIBLE;

public class NotPossibleMessage extends DataMessage<String> {

    public NotPossibleMessage() {
    }

    public NotPossibleMessage(String message, Date timestamp) {
        super(NOT_POSSIBLE, timestamp);
        this.setData(message);
    }
}
