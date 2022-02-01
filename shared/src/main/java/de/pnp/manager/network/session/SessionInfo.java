package de.pnp.manager.network.session;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionInfo {

    protected String sessionID;
    protected String sessionName;
    protected int actualClients;
    /**
     * -1 := unlimited clients
     */
    protected int maxClients;
    protected boolean passwordProtected;
    protected String host;
    protected String info;

    public SessionInfo(
            @JsonProperty("sessionID") String sessionID,
            @JsonProperty("sessionName") String sessionName,
            @JsonProperty("actualClients") int actualClients,
            @JsonProperty("maxClients") int maxClients,
            @JsonProperty("passwordProtected") boolean passwordProtected,
            @JsonProperty("host") String host,
            @JsonProperty("info") String info) {
        this.sessionID = sessionID;
        this.sessionName = sessionName;
        this.actualClients = actualClients;
        this.maxClients = maxClients;
        this.passwordProtected = passwordProtected;
        this.host = host;
        this.info = info;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public int getActualClients() {
        return actualClients;
    }

    public int getMaxClients() {
        return maxClients;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public String getHost() {
        return host;
    }

    public String getInfo() {
        return info;
    }
}
