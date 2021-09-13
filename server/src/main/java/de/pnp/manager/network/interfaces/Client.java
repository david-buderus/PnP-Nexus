package de.pnp.manager.network.interfaces;

import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.message.BaseMessage;

import java.util.function.Consumer;

public interface Client extends IClient {

    void sendMessage(BaseMessage message);

    void setOnDisconnect(Consumer<Client> onDisconnect);
}
