package de.pnp.manager.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.pnp.manager.main.Utility;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.INetworkClient;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.serializer.ServerModule;
import de.pnp.manager.network.session.BasicSession;
import de.pnp.manager.network.session.ISession;
import de.pnp.manager.network.session.Session;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static javafx.application.Platform.runLater;

public class ServerNetworkHandler implements NetworkHandler, Closeable {

    protected ServerSocket serverSocket;
    protected Thread serverThread;
    protected boolean active;

    protected Map<String, Client> clientMap;
    protected ListProperty<Client> clients;
    protected ObjectProperty<Session> activeSession;
    protected final Manager manager;

    public ServerNetworkHandler(Manager manager) {
        this.active = false;
        this.clientMap = Collections.synchronizedMap(new HashMap<>());
        this.activeSession = new SimpleObjectProperty<>(new Session("0", "Test", null, "", "DM", -1, Utility.getConfig()));
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
    public void addSession(ISession session) {
        this.activeSession.set((Session) session);
    }

    @Override
    public void removeSession(String sessionID) {
        if (this.activeSession.get() != null && this.activeSession.get().getSessionID().equals(sessionID)) {
            this.activeSession.set(null);
        }
    }

    @Override
    public void broadcast(BaseMessage message, Collection<? extends INetworkClient> clients) {
        for (INetworkClient client : clients) {
            if (client != null) {
                client.sendMessage(message);
            }
        }
    }

    @Override
    public void activeBroadcast(BaseMessage message, Collection<? extends INetworkClient> clients) {
        for (INetworkClient client : clients) {
            if (client != null) {
                client.sendActiveMessage(message);
            }
        }
    }

    @Override
    public boolean saveSession(String sessionID) {
        if (activeSession.get() == null || !activeSession.get().getSessionID().equals(sessionID)) {
            return false;
        }

        Session session = activeSession.get();
        Path sessionsPath = Paths.get(System.getProperty("user.home"), Utility.getConfig().getString("home.folder"), "sessions");

        final Path directoryPath = sessionsPath.resolve(sessionID);
        final File directory = directoryPath.toFile();

        if (!directory.exists() && !directory.mkdirs()) {
            return false;
        }

        final Path charactersPath = directoryPath.resolve("characters");
        final File charactersDirectory = charactersPath.toFile();

        if (!charactersDirectory.exists() && !charactersDirectory.mkdirs()) {
            return false;
        }

        final Path inventoriesPath = directoryPath.resolve("inventories");
        final File inventoriesDirectory = inventoriesPath.toFile();

        if (!inventoriesDirectory.exists() && !inventoriesDirectory.mkdirs()) {
            return false;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ServerModule());

        try {
            final ObjectWriter sessionWriter = mapper.writerFor(BasicSession.class);
            sessionWriter.writeValue(directoryPath.resolve("info.json").toFile(), session);

            for (PnPCharacter character : manager.getCharacterHandler().getCharacters(sessionID)) {
                mapper.writeValue(charactersPath.resolve(character.getCharacterID() + ".json").toFile(), character);
            }

            for (Container container : manager.getInventoryHandler().getContainers(sessionID)) {
                mapper.writeValue(charactersPath.resolve(container.getInventoryID() + ".json").toFile(), container);
            }

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @Override
    public ReadOnlyObjectProperty<Session> activeSessionProperty() {
        return activeSession;
    }

    @Override
    public ListProperty<Client> clientsProperty() {
        return clients;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void close() throws IOException {
        this.active = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
