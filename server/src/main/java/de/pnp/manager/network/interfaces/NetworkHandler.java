package de.pnp.manager.network.interfaces;

import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.session.ISession;

import java.util.Collection;

public interface NetworkHandler {

    void broadcast(BaseMessage message);

    default void broadcast(BaseMessage message, ISession session) {
        sendTo(message, session.getParticipatingClients().stream().map(IClient::getClientID).toArray(String[]::new));
    }

    void sendTo(BaseMessage message, String... clientIDs);

    Collection<? extends ISession> getActiveSessions();
}
