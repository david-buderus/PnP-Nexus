package de.pnp.manager.network.message.error;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.DENIED;

public class DeniedMessage extends ErrorMessage<String> {

    public DeniedMessage() {
    }

    public DeniedMessage(String message, Date timestamp) {
        super(DENIED, timestamp);
        this.setData(message);
    }
}
