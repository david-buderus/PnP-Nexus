package de.pnp.manager.network.message.universal;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.OK;

public class OkMessage extends BaseMessage {

    public OkMessage() {
    }

    public OkMessage(Date timestamp) {
        super(OK, timestamp);
    }
}
