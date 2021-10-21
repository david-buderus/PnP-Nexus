package de.pnp.manager.network;

import de.pnp.manager.main.Utility;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.session.ISession;
import de.pnp.manager.network.session.Session;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.stream.Collectors;

import static javafx.application.Platform.runLater;

public class ServerNetworkHandler implements NetworkHandler, Closeable {

    protected ServerSocket serverSocket;
    protected Thread serverThread;
    protected boolean active;

    protected Map<String, Client> clientMap;
    protected ListProperty<Client> clients;
    protected List<Session> sessions;
    protected final Manager manager;

    public ServerNetworkHandler(Manager manager) {
        this.active = false;
        this.clientMap = Collections.synchronizedMap(new HashMap<>());
        this.sessions = Collections.singletonList(new Session("0", "PnP"));
        this.clients = new SimpleListProperty<>(FXCollections.observableArrayList(c -> new Observable[]{c.currentSessionProperty()}));
        this.manager = manager;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(Utility.getConfig().getInt("server.port"));
            active = true;

            serverThread = new Thread(() -> {
                while (active) {
                    try {
                        ClientHandler client = new ClientHandler(serverSocket.accept(), manager);
                        client.setOnDisconnect(c -> {
                            clientMap.remove(c.getClientID());
                            if (client.getCurrentSession() != null) {
                                client.getCurrentSession().removeClient(c);
                            }
                            runLater(() -> clients.remove(c));
                        });
                        client.setDaemon(true);
                        client.start();
                        clientMap.put(client.getClientID(), client);
                        runLater(() -> clients.add(client));
                    } catch (IOException ignored) {
                    }
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void broadcast(BaseMessage message) {
        for (Client client : this.clientMap.values()) {
            client.sendMessage(message);
        }
    }

    @Override
    public void sendTo(BaseMessage message, String... clientIDs) {
        for (String clientID : clientIDs) {
            Client client = this.clientMap.get(clientID);

            if (client != null) {
                client.sendMessage(message);
            }
        }
    }

    @Override
    public void broadcast(BaseMessage message, Collection<Client> clients) {
        for (Client client : clients) {
            if (client != null) {
                client.sendMessage(message);
            }
        }
    }

    @Override
    public void activeBroadcast(BaseMessage message, Collection<Client> clients) {
        for (Client client : clients) {
            if (client != null) {
                client.sendActiveMessage(message);
            }
        }
    }

    @Override
    public Collection<? extends ISession> getActiveSessions() {
        return sessions;
    }

    @Override
    public ListProperty<Client> clientsProperty() {
        return clients;
    }

    @Override
    public void close() throws IOException {
        this.active = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
