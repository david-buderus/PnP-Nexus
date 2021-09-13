package de.pnp.manager.network.message.login;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.LOGOUT_REQUEST;

public class LogoutRequestMessage extends BaseMessage {

    public LogoutRequestMessage() {
    }

    public LogoutRequestMessage(Date timestamp) {
        super(LOGOUT_REQUEST, timestamp);
    }
}
