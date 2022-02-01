package de.pnp.manager.network;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.pnp.manager.main.Database;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.network.client.IClient;
import de.pnp.manager.network.eventhandler.AssignCharacterHandler;
import de.pnp.manager.network.eventhandler.LeaveSessionHandler;
import de.pnp.manager.network.eventhandler.RevokeCharacterHandler;
import de.pnp.manager.network.eventhandler.equipment.ChangeEquippedWeaponsHandler;
import de.pnp.manager.network.eventhandler.equipment.EquipHandler;
import de.pnp.manager.network.eventhandler.equipment.UnequipHandler;
import de.pnp.manager.network.eventhandler.inventory.*;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.interfaces.INetworkHandler;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.ControlledCharacterResponseMessage;
import de.pnp.manager.network.message.character.update.talent.UpdateTalentsData;
import de.pnp.manager.network.message.character.update.talent.UpdateTalentsRequestMessage;
import de.pnp.manager.network.message.database.DatabaseResponseMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotFoundMessage;
import de.pnp.manager.network.message.error.WrongStateMessage;
import de.pnp.manager.network.message.inventory.AccessibleContainerResponseMessage;
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

import java.net.Socket;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.pnp.manager.main.LanguageUtility.getMessage;
import static de.pnp.manager.network.message.MessageIDs.*;
import static javafx.application.Platform.runLater;

@JsonSerialize(as = IClient.class)
public class ClientHandler extends AbstractClientHandler<Manager> implements Client {

    private static short ID_COUNTER = 0;

    protected static synchronized String getNextClientID() {
        return String.format(LanguageUtility.getCurrentLanguage().getLocale(), "C-%05d", ++ID_COUNTER);
    }

    protected StringProperty clientName;
    protected ObjectProperty<Session> currentSession;

    public ClientHandler(Socket socket, Manager manager) {
        super(manager, socket, getNextClientID());
        this.mapper.registerModule(new ServerModule());
        this.clientName = new SimpleStringProperty(clientId);
        this.currentSession = new SimpleObjectProperty<>(null);
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
    protected StateMachine<BaseMessage> createStateMachine() {
        BaseMessageStateMachine stateMachine = new BaseMessageStateMachine(States.STATES, States.START);
        stateMachine.setOnNoTransition(event -> sendMessage(new WrongStateMessage(getMessage("message.error.wrongState"), calendar.getTime())));

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

            INetworkHandler handler = manager.getNetworkHandler();
            Optional<? extends ISession> optSession = handler.getActiveSessions().stream()
                    .filter(s -> s.getSessionID().equals(message.getData().getSessionId())).findFirst();

            if (optSession.isPresent()) {
                Session session = (Session) optSession.get();
                session.addClient(this);
                runLater(() -> setCurrentSession(session));
                sendMessage(new JoinSessionResponseMessage(session, calendar.getTime()));
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
                                        .stream().filter(c -> controlledCharacters.contains(c.getCharacterID()))
                                        .collect(Collectors.toList()),
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
                                        .stream().filter(c -> controlledCharacters.contains(c.getCharacterID()))
                                        .collect(Collectors.toList()),
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
                        sendMessage(new DeniedMessage(getMessage("message.error.denied.character"), calendar.getTime()));
                    }

                }
        );

        stateMachine.registerTransition(States.IN_CHARACTER, ASSIGN_CHARACTERS, new AssignCharacterHandler(this));
        stateMachine.registerTransition(States.IN_CHARACTER, States.IN_SESSION, REVOKE_CHARACTERS, new RevokeCharacterHandler(this));
        stateMachine.registerTransition(States.IN_CHARACTER, ASSIGN_INVENTORIES, new AssignInventoryHandler(this));
        stateMachine.registerTransition(States.IN_CHARACTER, REVOKE_INVENTORIES, new RevokeInventoryHandler(this));

        stateMachine.registerTransition(States.IN_CHARACTER, CREATE_ITEM_REQUEST, new CreateItemHandler(this, calendar, manager));
        stateMachine.registerTransition(States.IN_CHARACTER, DELETE_ITEM_REQUEST, new DeleteItemHandler(this, calendar, manager));
        stateMachine.registerTransition(States.IN_CHARACTER, MOVE_ITEM_REQUEST, new MoveItemHandler(this, calendar, manager));
        stateMachine.registerTransition(States.IN_CHARACTER, FABRICATE_ITEM_REQUEST, new FabricateItemHandler(this, calendar, manager));
        stateMachine.registerTransition(States.IN_CHARACTER, EQUIP_REQUEST, new EquipHandler(this, calendar, manager));
        stateMachine.registerTransition(States.IN_CHARACTER, UNEQUIP_REQUEST, new UnequipHandler(this, calendar, manager));
        stateMachine.registerTransition(States.IN_CHARACTER, CHANGE_EQUIPPED_WEAPONS, new ChangeEquippedWeaponsHandler(this, calendar, manager));

        return stateMachine;
    }
}
