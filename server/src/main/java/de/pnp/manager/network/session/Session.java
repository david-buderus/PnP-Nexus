package de.pnp.manager.network.session;

import de.pnp.manager.network.interfaces.Client;

import java.util.ArrayList;
import java.util.Collection;

public class Session implements ISession {

    protected String sessionID;
    protected String sessionName;
    protected ArrayList<Client> clients;

    public Session(String sessionID, String sessionName) {
        this.sessionID = sessionID;
        this.sessionName = sessionName;
        this.clients = new ArrayList<>();
    }

    public void addClient(Client client) {
        this.clients.add(client);
    }

    public void removeClient(Client client) {
        this.clients.remove(client);
    }

    @Override
    public String getSessionID() {
        return sessionID;
    }

    @Override
    public String getSessionName() {
        return sessionName;
    }

    @Override
    public Collection<Client> getParticipatingClients() {
        return clients;
    }
}
