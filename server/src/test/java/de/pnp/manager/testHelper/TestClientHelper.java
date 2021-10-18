package de.pnp.manager.testHelper;

import de.pnp.manager.main.Utility;
import de.pnp.manager.model.character.IPlayerCharacter;
import de.pnp.manager.model.character.Inventory;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.character.PnPCharacterFactory;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.character.AssignCharactersMessage;
import de.pnp.manager.network.message.inventory.AssignInventoryMessage;
import de.pnp.manager.network.message.login.LoginRequestMessage;
import de.pnp.manager.network.message.login.LoginResponseMessage;
import de.pnp.manager.network.message.session.JoinSessionRequestMessage;
import de.pnp.manager.network.message.session.QuerySessions;
import de.pnp.manager.network.message.session.SessionQueryResponse;

import java.io.IOException;
import java.util.Calendar;

public interface TestClientHelper {

    Calendar calender = Calendar.getInstance();

    default TestClient createTestClient() {
        TestClient client = new TestClient();
        client.connect("localhost", Utility.getConfig().getInt("server.port"));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return client;
    }

    default void prepareTestClient(TestClient client) {

        try {
            LoginResponseMessage loginResponse = (LoginResponseMessage) client.sendMessage(new LoginRequestMessage("Test", calender.getTime()));
            client.setClientID(loginResponse.getData().getId());

            SessionQueryResponse queryResponse = (SessionQueryResponse) client.sendMessage(new QuerySessions(calender.getTime()));
            String id = queryResponse.getData().stream().findFirst().orElseThrow().getSessionID();

            client.sendMessage(new JoinSessionRequestMessage(id, null, calender.getTime()));
            client.setSessionID(id);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default PlayerCharacter assignDefaultCharacter(Manager manager, TestClient client) {
        Client serverClient = manager.getNetworkHandler().clientsProperty().stream().filter(c -> c.getClientID().equals(client.getClientID())).findFirst().orElseThrow();
        PlayerCharacter character = manager.getCharacterHandler().createCharacter(client.getSessionID(), null,
                ((characterID, battle) -> PnPCharacterFactory.createDefaultCharacter(characterID, battle, PlayerCharacter::new)));
        serverClient.sendActiveMessage(new AssignCharactersMessage(character, calender.getTime()));

        try {
            AssignCharactersMessage message = (AssignCharactersMessage) client.receiveMessage();
            client.setCharacter((IPlayerCharacter) message.getData().stream().findFirst().orElseThrow());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return character;
    }

    default Container assignDefaultInventory(Manager manager, TestClient client) {
        Client serverClient = manager.getNetworkHandler().clientsProperty().stream().filter(c -> c.getClientID().equals(client.getClientID())).findFirst().orElseThrow();
        Container container = manager.getInventoryHandler().createContainer(client.getSessionID(), "Test Inventory", new Inventory(10, 1000));
        serverClient.sendActiveMessage(new AssignInventoryMessage(container, calender.getTime()));

        try {
            AssignInventoryMessage message = (AssignInventoryMessage) client.receiveMessage();
            client.setContainer((Container) message.getData().stream().findFirst().orElseThrow());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return container;
    }

    default TestClient createPreparedClient() {
        TestClient client = createTestClient();
        prepareTestClient(client);
        return client;
    }
}
