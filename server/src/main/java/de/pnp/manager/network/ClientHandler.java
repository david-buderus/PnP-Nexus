package de.pnp.manager.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.pnp.manager.main.Database;
import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.eventhandler.*;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.NetworkHandler;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.ControlledCharacterResponseMessage;
import de.pnp.manager.network.message.character.update.talent.UpdateTalentsData;
import de.pnp.manager.network.message.character.update.talent.UpdateTalentsRequestMessage;
import de.pnp.manager.network.message.database.DatabaseResponseMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotFoundMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.error.WrongStateMessage;
import de.pnp.manager.network.message.inventory.*;
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

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.server.UID;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.pnp.manager.main.LanguageUtility.getMessage;
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
    protected Collection<String> accessibleInventories;
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

    @Override
    public Collection<String> getAccessibleInventories() {
        return accessibleInventories;
    }

    @Override
    public boolean hasAccessToInventory(String id) {
        return controlledCharacters.contains(id) || accessibleInventories.contains(id);
    }

    private String getSessionID() {
        return getCurrentSession() != null ? getCurrentSession().getSessionID() : null;
    }

    private Collection<Client> getClientsWithInventoryAccess(String id) {
        return manager.getNetworkHandler().clientsProperty().stream().filter(c -> c.hasAccessToInventory(id)).collect(Collectors.toList());
    }

    private StateMachine<BaseMessage> createStateMachine() {
        BaseMessageStateMachine stateMachine = new BaseMessageStateMachine(States.STATES, States.START);
        stateMachine.setOnNoTransition(event -> {
            System.out.println(stateMachine);
            sendMessage(new WrongStateMessage(calendar.getTime()));
        });

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
            JoinSessionRequestMessage message = (JoinSessionRequestMessage) baseMessage;

            NetworkHandler handler = manager.getNetworkHandler();
            Optional<? extends ISession> optSession = handler.getActiveSessions().stream()
                    .filter(s -> s.getSessionID().equals(message.getData().getSessionId())).findFirst();

            if (optSession.isPresent()) {
                Session session = (Session) optSession.get();
                session.addClient(this);
                runLater(() -> setCurrentSession(session));
                sendMessage(new JoinSessionResponseMessage(session, calendar.getTime()));
                System.out.println("Joined Session");
            } else {
                sendMessage(new NotFoundMessage(getMessage("message.error.notExists"), calendar.getTime()));
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

        stateMachine.registerTransition(States.IN_SESSION, ACCESSIBLE_CONTAINER_REQUEST, message ->
                sendMessage(
                        new AccessibleContainerResponseMessage(
                                manager.getInventoryHandler().getContainers(getCurrentSession().getSessionID())
                                        .stream().filter(c -> hasAccessToInventory(c.getInventoryID())).collect(Collectors.toList()),
                                calendar.getTime()
                        )
                )
        );

        stateMachine.registerTransition(States.IN_SESSION, States.IN_CHARACTER, ASSIGN_CHARACTERS, new AssignCharacterHandler(this));
        stateMachine.registerTransition(States.IN_SESSION, ASSIGN_INVENTORIES, new AssignInventoryHandler(this));
        stateMachine.registerTransition(States.IN_SESSION, REVOKE_INVENTORIES, new RevokeInventoryHandler(this));

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
        stateMachine.registerTransition(States.IN_CHARACTER, ACCESSIBLE_CONTAINER_REQUEST, message ->
                sendMessage(
                        new AccessibleContainerResponseMessage(
                                manager.getInventoryHandler().getContainers(getCurrentSession().getSessionID())
                                        .stream().filter(c -> hasAccessToInventory(c.getInventoryID())).collect(Collectors.toList()),
                                calendar.getTime()
                        )
                )
        );

        stateMachine.registerTransition(States.IN_CHARACTER, DATABASE_REQUEST,
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

        stateMachine.registerTransition(States.IN_CHARACTER, UPDATE_TALENTS_REQUEST,
                message -> {
                    UpdateTalentsData data = ((UpdateTalentsRequestMessage) message).getData();

                    if (controlledCharacters.contains(data.getCharacterID())) {
                        PnPCharacter character = manager.getCharacterHandler().getCharacter(getCurrentSession().getSessionID(), data.getCharacterID());

                        if (character != null) {
                            runLater(() -> {
                                Map<ITalent, Integer> newValues = data.getNewValues();
                                for (ITalent talent : newValues.keySet()) {
                                    character.getObservableTalents().get(talent).set(newValues.get(talent));
                                }
                            });
                        } else {
                            sendMessage(new NotFoundMessage(getMessage("message.error.notExists"), calendar.getTime()));
                        }
                    } else {
                        sendMessage(new DeniedMessage(calendar.getTime()));
                    }

                }
        );

        stateMachine.registerTransition(States.IN_CHARACTER, ASSIGN_CHARACTERS, new AssignCharacterHandler(this));
        stateMachine.registerTransition(States.IN_CHARACTER, States.IN_SESSION, REVOKE_CHARACTERS, new RevokeCharacterHandler(this));
        stateMachine.registerTransition(States.IN_CHARACTER, ASSIGN_INVENTORIES, new AssignInventoryHandler(this));
        stateMachine.registerTransition(States.IN_CHARACTER, REVOKE_INVENTORIES, new RevokeInventoryHandler(this));

        stateMachine.registerTransition(States.IN_CHARACTER, CREATE_ITEM_REQUEST, message -> {
            CreateItemRequestMessage.CreateItemData data = ((CreateItemRequestMessage) message).getData();

            if (hasAccessToInventory(data.getInventoryID())) {
                IInventory inventory = manager.getInventoryHandler().getInventory(getSessionID(), data.getInventoryID());

                if (inventory != null && inventory.addAll(data.getItems())) {
                    manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                            data.getInventoryID(),
                            data.getItems(),
                            Collections.emptyList(),
                            calendar.getTime()
                    ), getClientsWithInventoryAccess(data.getInventoryID()));
                } else {
                    sendMessage(new NotPossibleMessage(getMessage("message.items.space.notEnough"), calendar.getTime()));
                }

            } else {
                sendMessage(new DeniedMessage(calendar.getTime()));
            }
        });

        stateMachine.registerTransition(States.IN_CHARACTER, DELETE_ITEM_REQUEST, message -> {
            DeleteItemRequestMessage.DeleteItemData data = ((DeleteItemRequestMessage) message).getData();

            if (hasAccessToInventory(data.getInventoryID())) {
                IInventory inventory = manager.getInventoryHandler().getInventory(getSessionID(), data.getInventoryID());

                if (inventory != null && inventory.removeAll(data.getItems())) {
                    manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                            data.getInventoryID(),
                            Collections.emptyList(),
                            data.getItems(),
                            calendar.getTime()
                    ), getClientsWithInventoryAccess(data.getInventoryID()));
                } else {
                    sendMessage(new NotPossibleMessage(getMessage("message.items.amount.notEnough"), calendar.getTime()));
                }

            } else {
                sendMessage(new DeniedMessage(calendar.getTime()));
            }
        });


        stateMachine.registerTransition(States.IN_CHARACTER, MOVE_ITEM_REQUEST, message -> {
            MoveItemRequestMessage.MoveItemData data = ((MoveItemRequestMessage) message).getData();

            if (hasAccessToInventory(data.getFrom())) {
                IInventory from = manager.getInventoryHandler().getInventory(getSessionID(), data.getFrom());

                if (from != null && from.containsAmount(data.getItems())) {

                    IInventory to = manager.getInventoryHandler().getInventory(getSessionID(), data.getTo());

                    if (to != null && to.addAll(data.getItems())) {
                        from.removeAll(data.getItems());

                        manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                                data.getFrom(),
                                Collections.emptyList(),
                                data.getItems(),
                                calendar.getTime()
                        ), getClientsWithInventoryAccess(data.getFrom()));
                        manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                                data.getTo(),
                                data.getItems(),
                                Collections.emptyList(),
                                calendar.getTime()
                        ), getClientsWithInventoryAccess(data.getTo()));

                    } else {
                        sendMessage(new NotPossibleMessage(getMessage("message.items.space.notEnough"), calendar.getTime()));
                    }

                } else {
                    sendMessage(new NotPossibleMessage(getMessage("message.items.amount.notEnough"), calendar.getTime()));
                }

            } else {
                sendMessage(new DeniedMessage(calendar.getTime()));
            }
        });

        return stateMachine;
    }
}
