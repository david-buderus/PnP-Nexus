package de.pnp.manager.network.message.database;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.DATABASE_REQUEST;

public class DatabaseRequestMessage extends BaseMessage {

    public DatabaseRequestMessage() {
    }

    public DatabaseRequestMessage(Date timestamp) {
        super(DATABASE_REQUEST, timestamp);
    }
}
