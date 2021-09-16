package de.pnp.manager.network.message.session;

import de.pnp.manager.network.message.DataMessage;
import de.pnp.manager.network.session.SessionInfo;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.SESSION_QUERY_RESPONSE;

public class SessionQueryResponse extends DataMessage<Collection<? extends SessionInfo>> {

    public SessionQueryResponse() {
    }

    public SessionQueryResponse(Collection<? extends SessionInfo> sessions, Date timestamp) {
        super(SESSION_QUERY_RESPONSE, timestamp);
        this.setData(sessions);
    }
}
