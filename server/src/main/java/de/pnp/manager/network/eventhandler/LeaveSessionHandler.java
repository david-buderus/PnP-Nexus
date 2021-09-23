package de.pnp.manager.network.eventhandler;

import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.session.UpdateSessionMessage;
import de.pnp.manager.network.message.universal.OkMessage;
import de.pnp.manager.network.session.Session;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.Calendar;

import static javafx.application.Platform.runLater;

public class LeaveSessionHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected NetworkHandler handler;
    protected Calendar calendar;

    public LeaveSessionHandler(Client client, NetworkHandler handler, Calendar calendar) {
        this.client = client;
        this.handler = handler;
        this.calendar = calendar;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        Session session = client.getCurrentSession();
        session.removeClient(client);
        runLater(() -> client.setCurrentSession(null));
        client.getControlledCharacters().clear();
        client.sendMessage(new OkMessage());
        handler.broadcast(new UpdateSessionMessage(session, calendar.getTime()), session);
    }
}
