package de.pnp.manager.network.message.session;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Date;

public class QuerySessions extends BaseMessage {

    public QuerySessions() {
    }

    public QuerySessions(Date timestamp) {
        super(2200, timestamp);
    }
}
