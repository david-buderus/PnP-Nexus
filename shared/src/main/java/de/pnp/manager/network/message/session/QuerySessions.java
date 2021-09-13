package de.pnp.manager.network.message.session;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.QUERY_SESSIONS;

public class QuerySessions extends BaseMessage {

    public QuerySessions() {
    }

    public QuerySessions(Date timestamp) {
        super(QUERY_SESSIONS, timestamp);
    }
}
