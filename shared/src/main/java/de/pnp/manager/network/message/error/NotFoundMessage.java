package de.pnp.manager.network.message.error;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.NOT_FOUND;

public class NotFoundMessage extends ErrorMessage<String> {

    public NotFoundMessage() {
    }

    public NotFoundMessage(String text, Date timestamp) {
        super(NOT_FOUND, timestamp);
        this.setData(text);

    }
}
