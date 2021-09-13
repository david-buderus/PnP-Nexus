package de.pnp.manager.network.message.error;

import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.ERROR;

public class ErrorMessage extends DataMessage<String> {

    public ErrorMessage() {
    }

    public ErrorMessage(String message, Date timestamp) {
        super(ERROR, timestamp);
        this.setData(message);
    }
}
