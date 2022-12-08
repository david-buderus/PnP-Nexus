package de.pnp.manager.network.interfaces;

import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.session.ISession;
import javafx.beans.property.ListProperty;

import java.util.Collection;
import java.util.stream.Collectors;

public interface NetworkHandler {

    void broadcast(BaseMessage message);

    default void broadcast(BaseMessage message, ISession session) {
        sendTo(message, session.getParticipatingClients().stream().map(IClient::getClientID).toArray(String[]::new));
    }

    void broadcast(BaseMessage message, Collection<Client> clients);

    /**
     * This message can change the state of each client
     */
    void activeBroadcast(BaseMessage message, Collection<Client> clients);

    void sendTo(BaseMessage message, String... clientIDs);

    Collection<? extends ISession> getActiveSessions();

    ListProperty<Client> clientsProperty();

    default Collection<Client> getClientsWithInventoryAccess(String id) {
        return clientsProperty().stream().filter(c -> c.hasAccessToInventory(id)).collect(Collectors.toList());
    }

    default Collection<Client> getClientsWithCharacterControl(String id) {
        return clientsProperty().stream().filter(c -> c.getControlledCharacters().contains(id)).collect(Collectors.toList());
    }
}