package de.pnp.manager.network.session;

import de.pnp.manager.main.Utility;
import de.pnp.manager.network.interfaces.Client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public int getMaxClients() {
        return -1;
    }

    @Override
    public boolean isPasswordProtected() {
        return false;
    }

    @Override
    public String getHost() {
        return "Dungeon Master";
    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public Collection<Client> getParticipatingClients() {
        return clients;
    }

    @Override
    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        Utility.getConfig().getKeys().forEachRemaining(key -> config.put(key, Utility.getConfig().getProperty(key)));
        return config;
    }
}
