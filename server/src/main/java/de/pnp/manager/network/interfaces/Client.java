package de.pnp.manager.network.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.session.Session;
import javafx.beans.property.StringProperty;

import java.util.function.Consumer;

public interface Client extends IClient {

    void sendMessage(BaseMessage message);

    void setOnDisconnect(Consumer<Client> onDisconnect);

    @JsonIgnore
    Session getCurrentSession();

    @JsonIgnore
    void setCurrentSession(Session session);

    @JsonIgnore
    StringProperty clientNameProperty();
}
