package de.pnp.manager.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.pnp.manager.main.Database;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.eventhandler.AssignCharacterHandler;
import de.pnp.manager.network.eventhandler.DismissCharacterHandler;
import de.pnp.manager.network.eventhandler.LeaveSessionHandler;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.ControlledCharacterResponseMessage;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.pnp.manager.network.message.MessageIDs.*;
import static javafx.application.Platform.runLater;

@JsonSerialize(as = IClient.class)
public class ClientHandler extends Thread implements Client {

    private static short ID_COUNTER = 0;

    protected static synchronized String getNextClientID() {
        return "C-" + new UID(++ID_COUNTER);
    }

    protected Socket clientSocket;
    protected PrintWriter out;
    protected BufferedReader in;
    protected ObjectMapper mapper;
    protected Calendar calendar;
    protected StateMachine<BaseMessage> stateMachine;

    protected String clientId;
    protected StringProperty clientName;
    protected ObjectProperty<Session> currentSession;
    protected Collection<String> controlledCharacters;
    protected final Manager manager;

    protected Consumer<Client> onDisconnect;

    public ClientHandler(Socket socket, Manager manager) {
        this.clientSocket = socket;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new ServerModule());
        this.calendar = Calendar.getInstance();
        this.clientId = getNextClientID();
        this.clientName = new SimpleStringProperty(clientId);
        this.currentSession = new SimpleObjectProperty<>(null);
        this.manager = manager;
        this.stateMachine = createStateMachine();
        this.controlledCharacters = new ArrayList<>();
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
    public void sendActiveMessage(BaseMessage message) {
        if (stateMachine.fire(message)) {
            sendMessage(message);
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
    public ObjectProperty<Session> currentSessionProperty() {
        return currentSession;
    }

    @Override
    public Collection<String> getControlledCharacters() {
        return controlledCharacters;
    }

    private StateMachine<BaseMessage> createStateMachine() {
        BaseMessageStateMachine stateMachine = new BaseMessageStateMachine(States.STATES, States.START);
        stateMachine.setOnNoTransition(event -> sendMessage(new WrongStateMessage(calendar.getTime())));

        // Pre login
        stateMachine.registerTransition(States.PRE_LOGIN, States.LOGGED_IN, LOGIN_REQUEST, message -> {
            LoginRequestMessage.LoginRequestData data = ((LoginRequestMessage) message).getData();
            runLater(() -> clientName.set(data.getName()));
            sendMessage(new LoginResponseMessage(clientId, data.getName(), calendar.getTime()));
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
                runLater(() -> setCurrentSession(session));
                sendMessage(new JoinSessionResponseMessage(session, calendar.getTime()));
            } else {
                sendMessage(new ErrorMessage("The session with the given id does not exist", calendar.getTime()));
            }
        });

        //In Session
        stateMachine.registerTransition(States.IN_SESSION, States.LOGGED_IN, LEAVE_SESSION_REQUEST,
                new LeaveSessionHandler(this, manager.getNetworkHandler(), calendar));

        stateMachine.registerTransition(States.IN_SESSION, DATABASE_REQUEST,
                message -> sendMessage(
                        new DatabaseResponseMessage(
                                Database.itemList,
                                Database.talentList,
                                Database.fabricationList,
                                Database.upgradeModelList,
                                calendar.getTime()
                        )
                )
        );

        stateMachine.registerTransition(States.IN_SESSION, CONTROLLED_CHARACTER_REQUEST, message ->
                sendMessage(
                        new ControlledCharacterResponseMessage(
                                manager.getCharacterHandler().getCharacters(getCurrentSession().getSessionID())
                                        .filtered(c -> controlledCharacters.contains(c.getCharacterID())),
                                calendar.getTime()
                        )
                )
        );

        stateMachine.registerTransition(States.IN_SESSION, States.IN_CHARACTER, ASSIGN_CHARACTERS, new AssignCharacterHandler(this));

        //In Character
        stateMachine.registerTransition(States.IN_CHARACTER, States.LOGGED_IN, LEAVE_SESSION_REQUEST,
                new LeaveSessionHandler(this, manager.getNetworkHandler(), calendar));
        stateMachine.registerTransition(States.IN_CHARACTER, CONTROLLED_CHARACTER_REQUEST, message ->
                sendMessage(
                        new ControlledCharacterResponseMessage(
                                manager.getCharacterHandler().getCharacters(getCurrentSession().getSessionID())
                                        .filtered(c -> controlledCharacters.contains(c.getCharacterID())),
                                calendar.getTime()
                        )
                )
        );

        stateMachine.registerTransition(States.IN_CHARACTER, ASSIGN_CHARACTERS, new AssignCharacterHandler(this));
        stateMachine.registerTransition(States.IN_CHARACTER, States.IN_SESSION, DISMISS_CHARACTERS, new DismissCharacterHandler(this));

        return stateMachine;
    }
}
