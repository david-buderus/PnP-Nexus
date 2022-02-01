package de.pnp.manager.network.interfaces;

import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.session.ISession;

import java.util.Collection;
import java.util.function.Consumer;

public interface INetworkClient extends IClient {

    void sendMessage(BaseMessage message);

    /**
     * This message can change the state of the client
     */
    void sendActiveMessage(BaseMessage message);

    void setOnDisconnect(Consumer<INetworkClient> onDisconnect);

    ISession getCurrentSession();

    void setCurrentSession(ISession session);

    Collection<String> getControlledCharacters();

    Collection<String> getAccessibleInventories();

    boolean hasAccessToInventory(String id);

    default String getSessionID() {
        return getCurrentSession() != null ? getCurrentSession().getSessionID() : null;
    }
}
