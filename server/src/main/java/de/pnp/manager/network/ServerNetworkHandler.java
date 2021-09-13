package de.pnp.manager.network;

import de.pnp.manager.main.Utility;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.session.ISession;
import de.pnp.manager.network.session.Session;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class ServerNetworkHandler implements NetworkHandler {

    protected ServerSocket serverSocket;
    protected Thread serverThread;
    protected boolean active;

    protected Map<String, Client> clients;
    protected List<Session> sessions;
    protected final Manager manager;

    public ServerNetworkHandler(Manager manager) {
        this.active = false;
        this.clients = Collections.synchronizedMap(new HashMap<>());
        this.sessions = Collections.singletonList(new Session("0", "PnP"));
        this.manager = manager;
    }

    public void stop() {
        try {
            this.active = false;
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {

        try {


            serverSocket = new ServerSocket(Utility.getConfig().getInt("server.port"));
            active = true;

            serverThread = new Thread(() -> {
                while (active) {
                    try {
                        ClientHandler client =  new ClientHandler(serverSocket.accept(), manager);
                        client.setOnDisconnect(c -> clients.remove(c.getClientID()));
                        client.setDaemon(true);
                        client.start();
                        clients.put(client.getClientID(), client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();

        } catch (IOException /* | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | NoSuchProviderException | KeyManagementException*/ e) {
            e.printStackTrace();
        }
    }

    @Override
    public void broadcast(BaseMessage message) {
        for (Client client : this.clients.values()) {
            client.sendMessage(message);
        }
    }

    @Override
    public void sendTo(BaseMessage message, String... clientIDs) {
        for (String clientID : clientIDs) {
            Client client = this.clients.get(clientID);

            if (client != null) {
                client.sendMessage(message);
            }
        }
    }

    @Override
    public Collection<? extends ISession> getActiveSessions() {
        return sessions;
    }
}
