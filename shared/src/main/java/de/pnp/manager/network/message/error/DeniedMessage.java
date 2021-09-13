package de.pnp.manager.network.message.error;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.DENIED;

public class DeniedMessage extends BaseMessage {

    public DeniedMessage() {
    }

    public DeniedMessage(Date timestamp) {
        super(DENIED, timestamp);
    }
}
