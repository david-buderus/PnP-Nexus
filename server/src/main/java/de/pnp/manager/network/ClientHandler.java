package de.pnp.manager.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.main.Database;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.eventhandler.LeaveSessionHandler;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.database.DatabaseResponseMessage;
import de.pnp.manager.network.message.error.ErrorMessage;
import de.pnp.manager.network.message.error.WrongStateMessage;
import de.pnp.manager.network.message.login.LoginRequestMessage;
import de.pnp.manager.network.message.login.LoginResponseMessage;
import de.pnp.manager.network.message.session.JoinSessionRequestMessage;
import de.pnp.manager.network.message.session.JoinSessionResponseMessage;
import de.pnp.manager.network.message.session.SessionQueryResponse;
import de.pnp.manager.network.message.universal.OkMessage;
import de.pnp.manager.network.serializer.ServerModule;
import de.pnp.manager.network.session.ISession;
import de.pnp.manager.network.session.Session;
import de.pnp.manager.network.state.BaseMessageStateMachine;
import de.pnp.manager.network.state.StateMachine;
import de.pnp.manager.network.state.States;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.pnp.manager.network.message.MessageIDs.*;
import static javafx.application.Platform.runLater;

public class ClientHandler extends Thread implements Client {

    private static int ID_COUNTER = 0;

    protected static synchronized String getNextClientID() {
        return "client-" + DigestUtils.sha256Hex(String.valueOf(++ID_COUNTER));
    }

    protected Socket clientSocket;
    protected PrintWriter out;
    protected BufferedReader in;
    protected ObjectMapper mapper;
    protected Calendar calendar;
    protected StateMachine<BaseMessage> stateMachine;

    protected String clientId;
    protected StringProperty clientName;
    protected Session currentSession;
    protected final Manager manager;

    protected Consumer<Client> onDisconnect;

    public ClientHandler(Socket socket, Manager manager) {
        this.clientSocket = socket;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new ServerModule());
        this.calendar = Calendar.getInstance();
        this.clientId = getNextClientID();
        this.clientName = new SimpleStringProperty(clientId);
        this.manager = manager;
        this.stateMachine = createStateMachine();
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
    public void setOnDisconnect(Consumer<Client> onDisconnect) {
        this.onDisconnect = onDisconnect;
    }

    @Override
    public String getClientID() {
        return clientId;
    }

    @Override
    public String getClientName() {
        return clientName.get();
    }

    @Override
    public StringProperty clientNameProperty() {
        return clientName;
    }

    @Override
    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session session) {
        this.currentSession = session;
    }

    private StateMachine<BaseMessage> createStateMachine() {
        BaseMessageStateMachine stateMachine = new BaseMessageStateMachine(States.STATES, States.START);
        stateMachine.setOnNoTransition(event -> sendMessage(new WrongStateMessage(calendar.getTime())));

        // Pre login
        stateMachine.registerTransition(States.PRE_LOGIN, States.LOGGED_IN, LOGIN_REQUEST, message -> {
            LoginRequestMessage.LoginRequestData data = ((LoginRequestMessage) message).getData();
            runLater(() -> clientName.set(data.getName()));
            sendMessage(new LoginResponseMessage(clientId, clientName.get(), calendar.getTime()));
        });

        // Logged in
        stateMachine.registerTransition(States.LOGGED_IN, QUERY_SESSIONS, message ->
                sendMessage(
                        new SessionQueryResponse(
                                manager.getNetworkHandler().getActiveSessions()
                                        .stream().map(ISession::getSessionInfo)
                                        .collect(Collectors.toList()),
                                calendar.getTime()
                        )
                )
        );

        stateMachine.registerTransition(States.LOGGED_IN, States.PRE_LOGIN, LOGOUT_REQUEST, message -> {
            runLater(() -> clientName.set(clientId));
            sendMessage(new OkMessage(calendar.getTime()));
        });

        stateMachine.registerTransition(States.LOGGED_IN, States.IN_SESSION, JOIN_SESSION_REQUEST, baseMessage -> {
            JoinSessionRequestMessage message =  (JoinSessionRequestMessage) baseMessage;

            NetworkHandler handler = manager.getNetworkHandler();
            Optional<? extends ISession> optSession = handler.getActiveSessions().stream()
                    .filter(s -> s.getSessionID().equals(message.getData().getSessionId())).findFirst();

            if (optSession.isPresent()) {
                Session session = (Session) optSession.get();
                session.addClient(this);
                this.currentSession = session;
                sendMessage(new JoinSessionResponseMessage(session, calendar.getTime()));
            } else {
                sendMessage(new ErrorMessage("The session with the given id does not exist", calendar.getTime()));
            }
        });

        //In Session
        stateMachine.registerTransition(States.IN_SESSION, States.LOGGED_IN, LEAVE_SESSION_REQUEST,
                message -> new LeaveSessionHandler(this, manager.getNetworkHandler(), calendar));

        stateMachine.registerTransition(States.IN_SESSION, DATABASE_REQUEST,
                message -> sendMessage(
                        new DatabaseResponseMessage(
                                Database.itemList,
                                Database.fabricationList,
                                Database.upgradeModelList,
                                calendar.getTime()
                        )
                )
        );

        //In Character
        stateMachine.registerTransition(States.IN_CHARACTER, States.LOGGED_IN, LEAVE_SESSION_REQUEST,
                message -> new LeaveSessionHandler(this, manager.getNetworkHandler(), calendar));

        return stateMachine;
    }
}
