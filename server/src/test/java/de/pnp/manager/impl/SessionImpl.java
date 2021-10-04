package de.pnp.manager.impl;

import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.session.ISession;

import java.util.Collection;
import java.util.Map;

public class SessionImpl implements ISession {

    protected String sessionID;
    protected String sessionName;
    protected int maxClients;
    protected boolean passwordProtected;
    protected String host;
    protected String info;
    protected Collection<? extends IClient> participatingClients;
    protected Map<String, Object> config;

    @Override
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    @Override
    public int getMaxClients() {
        return maxClients;
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    @Override
    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Collection<? extends IClient> getParticipatingClients() {
        return participatingClients;
    }

    public void setParticipatingClients(Collection<? extends IClient> participatingClients) {
        this.participatingClients = participatingClients;
    }

    @Override
    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
}
