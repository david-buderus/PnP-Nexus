package de.pnp.manager.network.message.session;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.LEAVE_SESSION_REQUEST;

public class LeaveSessionRequestMessage extends BaseMessage {

    public LeaveSessionRequestMessage() {
    }

    public LeaveSessionRequestMessage(Date timestamp) {
        super(LEAVE_SESSION_REQUEST, timestamp);
    }
}
