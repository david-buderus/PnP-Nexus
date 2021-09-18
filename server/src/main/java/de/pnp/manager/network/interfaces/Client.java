package de.pnp.manager.network.interfaces;

import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.session.Session;
import javafx.beans.property.StringProperty;

import java.util.Collection;
import java.util.function.Consumer;

public interface Client extends IClient {

    void sendMessage(BaseMessage message);

    /**
     * This message can change the state of the client
     */
    void sendActiveMessage(BaseMessage message);

    void setOnDisconnect(Consumer<Client> onDisconnect);

    Session getCurrentSession();

    void setCurrentSession(Session session);

    StringProperty clientNameProperty();

    Collection<String> getControlledCharacters();
}
