package de.pnp.manager.network.session;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BasicSession {

    protected String sessionID;
    protected String sessionName;
    protected String passwordHash;
    protected String info;

    @JsonCreator
    public BasicSession(
            @JsonProperty("sessionID") String sessionID,
            @JsonProperty("sessionName") String sessionName,
            @JsonProperty("passwordHash") String passwordHash,
            @JsonProperty("info") String info) {
        this.sessionID = sessionID;
        this.sessionName = sessionName;
        this.passwordHash = passwordHash;
        this.info = info;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @JsonIgnore
    public boolean isPasswordProtected() {
        return passwordHash != null;
    }

    public String getInfo() {
        return info;
    }
}
