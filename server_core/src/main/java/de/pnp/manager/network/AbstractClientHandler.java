package de.pnp.manager.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.manager.interfaces.IManager;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.interfaces.IBattle;
import de.pnp.manager.network.interfaces.INetworkClient;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.state.StateMachine;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.function.Consumer;

public abstract class AbstractClientHandler<Manager extends IManager<?, ?>> extends Thread implements INetworkClient {

    protected Socket clientSocket;
    protected PrintWriter out;
    protected BufferedReader in;
    protected ObjectMapper mapper;
    protected Calendar calendar;
    protected final Manager manager;
    protected StateMachine<BaseMessage> stateMachine;

    protected String clientId;
    protected Collection<String> controlledCharacters;
    protected Collection<String> accessibleInventories;

    protected Consumer<INetworkClient> onDisconnect;

    public AbstractClientHandler(Manager manager, Socket socket, String clientId) {
        this.manager = manager;
        this.clientSocket = socket;
        this.mapper = new ObjectMapper();
        this.calendar = Calendar.getInstance();
        this.clientId = clientId;
        this.stateMachine = createStateMachine();
        this.controlledCharacters = new ArrayList<>();
        this.accessibleInventories = new ArrayList<>();
    }

    public void run() {
        try {
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            String input;
            while ((input = in.readLine()) != null) {
                handleMessage(mapper.readValue(input, BaseMessage.class));
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.onDisconnect.accept(this);
    }

    protected void handleMessage(BaseMessage message) {
        stateMachine.fire(message);
    }

    @Override
    public void sendMessage(BaseMessage message) {
        try {
            out.println(mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendActiveMessage(BaseMessage message) {
        if (stateMachine.fire(message)) {
            sendMessage(message);
        }
    }

    @Override
    public void setOnDisconnect(Consumer<INetworkClient> onDisconnect) {
        this.onDisconnect = onDisconnect;
    }

    @Override
    public String getClientID() {
        return clientId;
    }

    @Override
    public Collection<String> getControlledCharacters() {
        return controlledCharacters;
    }

    @Override
    public Collection<String> getAccessibleInventories() {
        return accessibleInventories;
    }

    @Override
    public boolean hasAccessToInventory(String id) {
        return controlledCharacters.contains(id) || accessibleInventories.contains(id);
    }

    protected abstract StateMachine<BaseMessage> createStateMachine();
}
