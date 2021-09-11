package de.pnp.manager.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.DataMessage;
import de.pnp.manager.network.message.login.LoginRequestMessage;
import de.pnp.manager.network.message.login.LoginResponseMessage;
import de.pnp.manager.network.message.session.SessionQueryResponse;
import de.pnp.manager.network.serializer.ServerModule;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Collection;
import java.util.function.Consumer;

public class ClientHandler extends Thread implements Client {

    private static int ID_COUNTER = 0;

    protected static synchronized String getNextClientID(){
        return "client-" + DigestUtils.sha256Hex(String.valueOf(++ID_COUNTER));
    }

    protected Socket clientSocket;
    protected PrintWriter out;
    protected BufferedReader in;
    protected ObjectMapper mapper;
    protected Calendar calendar;

    protected String clientId;
    protected String clientName;
    protected final Manager manager;

    protected Consumer<Client> onDisconnect;

    public ClientHandler(Socket socket, Manager manager) {
        this.clientSocket = socket;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new ServerModule());
        this.calendar = Calendar.getInstance();
        this.clientId = getNextClientID();
        this.clientName = clientId;
        this.manager = manager;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

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
        switch (message.getType()) {
            case loginRequest:
                LoginRequestMessage.LoginRequestData data = ((LoginRequestMessage) message).getData();
                this.clientName = data.getName();
                sendMessage(new LoginResponseMessage(clientId, clientName, calendar.getTime()));
                break;

            case querySessions:
                sendMessage(new SessionQueryResponse(manager.getNetworkHandler().getActiveSessions(), calendar.getTime()));
                break;
        }
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
    public void setOnDisconnect(Consumer<Client> onDisconnect) {
        this.onDisconnect = onDisconnect;
    }

    @Override
    public String getClientID() {
        return clientId;
    }

    @Override
    public String getClientName() {
        return clientName;
    }
}
