package de.pnp.manager.network.interfaces;

import de.pnp.manager.network.session.ISession;
import de.pnp.manager.network.session.Session;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.Collection;
import java.util.Collections;

public interface NetworkHandler extends INetworkHandler {

    default Collection<? extends ISession> getActiveSessions() {
        return activeSessionProperty().get() != null ?
                Collections.singleton(activeSessionProperty().get()) : Collections.emptyList();
    }

    ReadOnlyObjectProperty<Session> activeSessionProperty();

    ListProperty<Client> clientsProperty();

    @Override
    default Collection<? extends INetworkClient> getClients() {
        return clientsProperty().get();
    }
}