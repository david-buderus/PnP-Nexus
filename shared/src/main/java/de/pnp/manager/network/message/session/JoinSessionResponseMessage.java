package de.pnp.manager.network.message.session;

import de.pnp.manager.network.message.DataMessage;
import de.pnp.manager.network.session.ISession;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.JOIN_SESSION_RESPONSE;

public class JoinSessionResponseMessage extends DataMessage<ISession> {

    public JoinSessionResponseMessage() {
    }

    public JoinSessionResponseMessage(ISession session, Date timestamp) {
        super(JOIN_SESSION_RESPONSE, timestamp);
        this.setData(session);
    }
}
