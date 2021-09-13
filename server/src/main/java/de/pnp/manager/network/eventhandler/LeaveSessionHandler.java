package de.pnp.manager.network.eventhandler;

import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.session.UpdateSessionMessage;
import de.pnp.manager.network.message.universal.OkMessage;
import de.pnp.manager.network.session.Session;

import java.util.Calendar;
import java.util.function.Consumer;

public class LeaveSessionHandler implements Consumer<BaseMessage> {

    protected Client client;
    protected NetworkHandler handler;
    protected Calendar calendar;

    public LeaveSessionHandler(Client client, NetworkHandler handler, Calendar calendar) {
        this.client = client;
        this.handler = handler;
        this.calendar = calendar;
    }

    @Override
    public void accept(BaseMessage message) {
        Session session = client.getCurrentSession();
        session.removeClient(client);
        client.setCurrentSession(null);
        client.sendMessage(new OkMessage());
        handler.broadcast(new UpdateSessionMessage(session, calendar.getTime()), session);
    }
}
