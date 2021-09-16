package de.pnp.manager.network.session;

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

    public SessionInfo() {
    }

    public SessionInfo(String sessionID, String sessionName, int actualClients, int maxClients, boolean passwordProtected, String host, String info) {
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

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getActualClients() {
        return actualClients;
    }

    public void setActualClients(int actualClients) {
        this.actualClients = actualClients;
    }

    public int getMaxClients() {
        return maxClients;
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
