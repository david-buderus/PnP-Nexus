package de.pnp.manager.network.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.network.client.IClient;

import java.util.Collection;
import java.util.Map;

public interface ISession {

    String getSessionID();

    String getSessionName();

    int getMaxClients();

    boolean isPasswordProtected();

    String getHost();

    String getInfo();

    Collection<? extends IClient> getParticipatingClients();

    Map<String, Object> getConfig();

    @JsonIgnore
    default SessionInfo getSessionInfo() {
        return new SessionInfo(getSessionID(), getSessionName(), getParticipatingClients().size(), getMaxClients(), isPasswordProtected(), getHost(), getInfo());
    }
}
