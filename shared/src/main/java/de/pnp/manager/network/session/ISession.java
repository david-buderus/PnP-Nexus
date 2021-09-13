package de.pnp.manager.network.session;

import de.pnp.manager.network.client.IClient;

import java.util.Collection;

public interface ISession {

    String getSessionID();

    String getSessionName();

    Collection<? extends IClient> getParticipatingClients();
}
