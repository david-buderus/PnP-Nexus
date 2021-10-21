package de.pnp.manager.network;

import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.item.Item;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.MessageIDs;
import de.pnp.manager.network.message.inventory.CreateItemRequestMessage;
import de.pnp.manager.network.message.inventory.DeleteItemRequestMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.network.message.inventory.MoveItemRequestMessage;
import de.pnp.manager.testHelper.TestClient;
import de.pnp.manager.testHelper.TestClientHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ResourceLock(value = "SERVER_SOCKET", mode = ResourceAccessMode.READ_WRITE)
@ExtendWith(ApplicationExtension.class)
public class InventoryTest implements TestClientHelper {

    protected static Manager manager;

    @BeforeAll
    @Test
    public static void setup() {
        manager = new Manager();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    @Test
    public static void close() {
        try {
            manager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createItemTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Item testItem = new Item();
        testItem.setName("Test");

        InventoryUpdateNotificationMessage message = (InventoryUpdateNotificationMessage) client.sendMessage(new CreateItemRequestMessage(playerCharacter.getCharacterID(), testItem, calender.getTime()));
        InventoryUpdateNotificationMessage.InventoryUpdateData data = message.getData().stream().findFirst().orElseThrow();

        assertTrue(playerCharacter.getInventory().contains(testItem));
        assertEquals(data.getInventoryID(), playerCharacter.getCharacterID());
        assertTrue(data.getAddedItems().contains(testItem));
        assertTrue(data.getRemovedItems().isEmpty());
    }

    @Test
    public void deleteItemTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        assignDefaultCharacter(manager, client);
        Container container = assignDefaultInventory(manager, client);

        Item testItem = new Item();
        testItem.setName("Test");

        BaseMessage update = client.sendMessage(new CreateItemRequestMessage(container.getInventoryID(), testItem, calender.getTime()));
        assertEquals(MessageIDs.UPDATE_INVENTORY_NOTIFICATION, update.getId());

        InventoryUpdateNotificationMessage message = (InventoryUpdateNotificationMessage) client.sendMessage(new DeleteItemRequestMessage(container.getInventoryID(), testItem, calender.getTime()));
        InventoryUpdateNotificationMessage.InventoryUpdateData data = message.getData().stream().findFirst().orElseThrow();

        assertTrue(container.getInventory().isEmpty());
        assertEquals(data.getInventoryID(), container.getInventoryID());
        assertTrue(data.getAddedItems().isEmpty());
        assertTrue(data.getRemovedItems().contains(testItem));
    }

    @Test
    public void moveItemTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);
        Container container = assignDefaultInventory(manager, client);

        String from = container.getInventoryID();
        String to = playerCharacter.getCharacterID();

        Item testItem = new Item();
        testItem.setName("Test");

        BaseMessage update = client.sendMessage(new CreateItemRequestMessage(container.getInventoryID(), testItem, calender.getTime()));
        assertEquals(MessageIDs.UPDATE_INVENTORY_NOTIFICATION, update.getId());

        InventoryUpdateNotificationMessage message1 = (InventoryUpdateNotificationMessage) client.sendMessage(new MoveItemRequestMessage(from, to, Collections.singleton(testItem), calender.getTime()));
        InventoryUpdateNotificationMessage message2 = (InventoryUpdateNotificationMessage) client.receiveMessage();
        InventoryUpdateNotificationMessage.InventoryUpdateData data1 = message1.getData().stream().findFirst().orElseThrow();
        InventoryUpdateNotificationMessage.InventoryUpdateData data2 = message2.getData().stream().findFirst().orElseThrow();

        assertTrue(data1.getInventoryID().equals(from) || data1.getInventoryID().equals(to));
        assertTrue(data2.getInventoryID().equals(from) || data2.getInventoryID().equals(to));

        // Need to check which message is the remove update and which is the add update
        if (data1.getInventoryID().equals(from)) {
            assertTrue(data1.getAddedItems().isEmpty());
            assertTrue(data1.getRemovedItems().contains(testItem));
        }
        if (data2.getInventoryID().equals(from)) {
            assertTrue(data2.getAddedItems().isEmpty());
            assertTrue(data2.getRemovedItems().contains(testItem));
        }
        if (data1.getInventoryID().equals(to)) {
            assertTrue(data1.getRemovedItems().isEmpty());
            assertTrue(data1.getAddedItems().contains(testItem));
        }
        if (data2.getInventoryID().equals(to)) {
            assertTrue(data2.getRemovedItems().isEmpty());
            assertTrue(data2.getAddedItems().contains(testItem));
        }

        assertTrue(playerCharacter.getInventory().contains(testItem));
        assertTrue(container.getInventory().isEmpty());
    }
}
