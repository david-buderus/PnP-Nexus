package de.pnp.manager.network;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.Fabrication;
import de.pnp.manager.model.IFabrication;
import de.pnp.manager.model.ItemList;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.item.Item;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.CreateItemRequestMessage;
import de.pnp.manager.network.message.inventory.FabricateItemRequestMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.testHelper.TestClient;
import de.pnp.manager.testHelper.TestClientHelper;
import de.pnp.manager.testHelper.TestWithDatabaseAccess;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ResourceLock(value = "SERVER_SOCKET", mode = ResourceAccessMode.READ_WRITE)
@ExtendWith(ApplicationExtension.class)
public class FabricationTest extends TestWithDatabaseAccess implements TestClientHelper {

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

    @Test
    public void simpleFabricateItemTest() throws IOException {
        TestClient client = createPreparedClient();
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        IFabrication fabrication = Database.fabricationList.stream().findFirst().orElseThrow();

        playerCharacter.getInventory().addAll(fabrication.getMaterials());

        BaseMessage message = client.sendMessage(new FabricateItemRequestMessage(playerCharacter.getCharacterID(), fabrication, calender.getTime()));
        InventoryUpdateNotificationMessage.InventoryUpdateData data = ((InventoryUpdateNotificationMessage) message).getData().stream().findFirst().orElseThrow();

        assertTrue(playerCharacter.getInventory().contains(fabrication.getProduct()));
        assertEquals(playerCharacter.getInventory().stream().findFirst().orElseThrow().getAmount(), fabrication.getProductAmount());
        assertEquals(data.getInventoryID(), playerCharacter.getCharacterID());
        assertTrue(data.getAddedItems().contains(fabrication.getProduct()));
        assertEquals(fabrication.getMaterials(), data.getRemovedItems());
    }

    @Test
    public void fabricateItemTest() throws IOException {
        TestClient client = createPreparedClient();
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);
        Container container1 = assignDefaultInventory(manager, client);
        Container container2 = assignDefaultInventory(manager, client);

        Item product = new Item("Product");
        Item sideProduct = new Item("SideProduct");
        Item material1 = new Item("material1");
        Item material2 = new Item("material2");
        material2.setAmount(3);
        Item material3 = new Item("material3");
        Item material4 = new Item("material4");
        material4.setAmount(2);

        Fabrication fabrication = new Fabrication();
        fabrication.setProduct(product);
        fabrication.setSideProductAmount(2);
        fabrication.setSideProduct(sideProduct);
        fabrication.setMaterials(new ItemList(material1, material2, material3, material4));

        // Server accepts only known fabrications
        Database.fabricationList.add(fabrication);

        //Prepare Inventories
        Item material2Stack1 = material2.copy();
        material2Stack1.setAmount(1);
        Item material2Stack2 = material2.copy();
        material2Stack2.setAmount(2);

        playerCharacter.getInventory().add(material1);
        container1.getInventory().add(material2Stack1);
        container2.getInventory().add(material2Stack2);
        container2.getInventory().add(material3);
        playerCharacter.getInventory().add(material4);

        BaseMessage message1 = client.sendMessage(
                new FabricateItemRequestMessage(
                        Arrays.asList(playerCharacter.getCharacterID(), container1.getInventoryID(), container2.getInventoryID()),
                        container1.getInventoryID(),
                        fabrication, calender.getTime()
                )
        );
        BaseMessage message2 = client.receiveMessage();
        BaseMessage message3 = client.receiveMessage();

        List<BaseMessage> messages = Arrays.asList(message1, message2, message3);

        assertTrue(
                messages.stream().map(message -> ((InventoryUpdateNotificationMessage) message).getData().stream()
                        .findFirst().orElseThrow().getInventoryID()).anyMatch(id -> id.equals(playerCharacter.getCharacterID()))
        );
        assertTrue(
                messages.stream().map(message -> ((InventoryUpdateNotificationMessage) message).getData().stream()
                        .findFirst().orElseThrow().getInventoryID()).anyMatch(id -> id.equals(container1.getInventoryID()))
        );
        assertTrue(
                messages.stream().map(message -> ((InventoryUpdateNotificationMessage) message).getData().stream()
                        .findFirst().orElseThrow().getInventoryID()).anyMatch(id -> id.equals(container2.getInventoryID()))
        );

        for (BaseMessage message : messages) {
            InventoryUpdateNotificationMessage.InventoryUpdateData data = ((InventoryUpdateNotificationMessage) message).getData().stream().findFirst().orElseThrow();

            if (data.getInventoryID().equals(playerCharacter.getCharacterID())) {

            } else if (data.getInventoryID().equals(container1.getInventoryID())) {

            } else if (data.getInventoryID().equals(container2.getInventoryID())) {

            } else {
                fail();
            }
        }

        /*
        assertTrue(playerCharacter.getInventory().contains(fabrication.getProduct()));
        assertEquals(playerCharacter.getInventory().stream().findFirst().orElseThrow().getAmount(), fabrication.getProductAmount());
        assertEquals(data.getInventoryID(), playerCharacter.getCharacterID());
        assertTrue(data.getAddedItems().contains(fabrication.getProduct()));
        assertEquals(fabrication.getMaterials(), data.getRemovedItems()); */
    }

    @Test
    public void fabricateItemWithoutEnoughItemsTest() throws IOException {
        TestClient client = createPreparedClient();
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        IFabrication fabrication = Database.fabricationList.stream().findFirst().orElseThrow();

        BaseMessage message = client.sendMessage(new FabricateItemRequestMessage(playerCharacter.getCharacterID(), fabrication, calender.getTime()));

        assertTrue(message instanceof NotPossibleMessage);
    }
}
