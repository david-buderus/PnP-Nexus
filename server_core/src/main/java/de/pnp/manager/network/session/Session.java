package de.pnp.manager.network.session;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.pnp.manager.network.interfaces.INetworkClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration2.Configuration;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(as = ISession.class)
public class Session extends BasicSession implements ISession {

    protected String host;
    protected int maxClients;
    protected ArrayList<INetworkClient> clients;
    protected Configuration config;

    public Session(String sessionID, String sessionName, char[] password, String info, String host, int maxClients, Configuration config) {
        super(sessionID, sessionName,
                password != null && password.length > 0 ?
                        DigestUtils.sha256Hex(StandardCharsets.UTF_8.encode(CharBuffer.wrap(password)).array()) :
                        null,
                info
        );
        this.host = host;
        this.maxClients = maxClients;
        this.config = config;
        this.clients = new ArrayList<>();
    }

    public Session(BasicSession base, String host, int maxClients) {
        super(base.getSessionID(), base.getSessionName(), base.getPasswordHash(), base.getInfo());
        this.host = host;
        this.maxClients = maxClients;
        this.clients = new ArrayList<>();
    }

    public void addClient(INetworkClient client) {
        this.clients.add(client);
    }

    public void removeClient(INetworkClient client) {
        this.clients.remove(client);
    }

    @Override
    public int getMaxClients() {
        return maxClients;
    }

    @Override
    public boolean isPasswordProtected() {
        return passwordHash != null;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public Collection<INetworkClient> getParticipatingClients() {
        return clients;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public Map<String, Object> getConfig() {
        Map<String, Object> result = new HashMap<>();
        for (String key : config.getStringArray("server.client.configuration.keys")) {
            result.put(key, config.getProperty(key));
        }
        return result;
    }
}
