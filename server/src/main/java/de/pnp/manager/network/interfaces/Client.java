package de.pnp.manager.network.interfaces;

import de.pnp.manager.network.session.ISession;
import de.pnp.manager.network.session.Session;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public interface Client extends INetworkClient {

    default Session getCurrentSession() {
        return currentSessionProperty().get();
    }

    default void setCurrentSession(ISession session) {
        currentSessionProperty().set((Session) session);
    }

    ObjectProperty<Session> currentSessionProperty();

    StringProperty clientNameProperty();
}