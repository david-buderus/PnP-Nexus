package de.pnp.manager.network.interfaces;

import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.session.ISession;

import java.util.Collection;
import java.util.stream.Collectors;

public interface INetworkHandler {

    void broadcast(BaseMessage message);

    default void broadcast(BaseMessage message, ISession session) {
        sendTo(message, session.getParticipatingClients().stream().map(IClient::getClientID).toArray(String[]::new));
    }

    void broadcast(BaseMessage message, Collection<? extends INetworkClient> clients);

    /**
     * This message can change the state of each client
     */
    void activeBroadcast(BaseMessage message, Collection<? extends INetworkClient> clients);

    void sendTo(BaseMessage message, String... clientIDs);

    Collection<? extends ISession> getActiveSessions();

    void addSession(ISession session);

    void removeSession(String sessionID);

    boolean saveSession(String sessionID);

    Collection<? extends INetworkClient> getClients();

    default Collection<? extends INetworkClient> getClientsWithInventoryAccess(String id) {
        return getClients().stream().filter(c -> c.hasAccessToInventory(id)).collect(Collectors.toList());
    }

    default Collection<? extends INetworkClient> getClientsWithCharacterControl(String id) {
        return getClients().stream().filter(c -> c.getControlledCharacters().contains(id)).collect(Collectors.toList());
    }
}
