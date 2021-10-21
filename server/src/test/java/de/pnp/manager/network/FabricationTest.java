package de.pnp.manager.network;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.Fabrication;
import de.pnp.manager.model.IFabrication;
import de.pnp.manager.model.ItemList;
import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.item.Item;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.FabricateItemRequestMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.testHelper.TestClient;
import de.pnp.manager.testHelper.TestClientHelper;
import de.pnp.manager.testHelper.TestWithDatabaseAccess;
import org.junit.jupiter.api.AfterAll;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

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
    public void simpleFabricateItemTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        IFabrication fabrication = Database.fabricationList.stream().findFirst().orElseThrow();

        playerCharacter.getInventory().addAll(fabrication.getMaterials());

        BaseMessage message = client.sendMessage(new FabricateItemRequestMessage(playerCharacter.getCharacterID(), fabrication, calender.getTime()));
        InventoryUpdateNotificationMessage.InventoryUpdateData data = ((InventoryUpdateNotificationMessage) message).getData().stream().findFirst().orElseThrow();

        assertThat(playerCharacter.getInventory()).contains(fabrication.getProduct());
        assertThat(playerCharacter.getInventory()).first().extracting(IItem::getAmount).isEqualTo(fabrication.getProductAmount());
        assertThat(playerCharacter.getCharacterID()).isEqualTo(data.getInventoryID());
        assertThat(data.getAddedItems()).asList().contains(fabrication.getProduct());
        assertThat(data.getRemovedItems()).isEqualTo(fabrication.getMaterials());
    }

    @Test
    public void fabricateItemTest() throws IOException {
        TestClient client = createPreparedClient(manager);
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

        InventoryUpdateNotificationMessage message1 = (InventoryUpdateNotificationMessage) client.sendMessage(
                new FabricateItemRequestMessage(
                        Arrays.asList(playerCharacter.getCharacterID(), container1.getInventoryID(), container2.getInventoryID()),
                        container1.getInventoryID(),
                        fabrication, calender.getTime()
                )
        );
        InventoryUpdateNotificationMessage message2 = (InventoryUpdateNotificationMessage) client.receiveMessage();
        InventoryUpdateNotificationMessage message3 = (InventoryUpdateNotificationMessage) client.receiveMessage();

        List<InventoryUpdateNotificationMessage> messages = Arrays.asList(message1, message2, message3);

        assertThat(messages).extracting(InventoryUpdateNotificationMessage::getData)
                .flatExtracting(data -> data.stream().map(InventoryUpdateNotificationMessage.InventoryUpdateData::getInventoryID).collect(Collectors.toList()))
                .containsExactlyInAnyOrder(playerCharacter.getCharacterID(), container1.getInventoryID(), container2.getInventoryID());

        for (InventoryUpdateNotificationMessage message : messages) {
            InventoryUpdateNotificationMessage.InventoryUpdateData data = message.getData().stream().findFirst().orElseThrow();

            if (data.getInventoryID().equals(playerCharacter.getCharacterID())) {

                assertThat(data).extracting(InventoryUpdateNotificationMessage.InventoryUpdateData::getAddedItems).asList().isEmpty();
                assertThat(data).extracting(InventoryUpdateNotificationMessage.InventoryUpdateData::getRemovedItems).asList()
                        .containsExactlyInAnyOrder(material1, material4);

            } else if (data.getInventoryID().equals(container1.getInventoryID())) {

                assertThat(data).extracting(InventoryUpdateNotificationMessage.InventoryUpdateData::getAddedItems).asList()
                        .containsExactlyInAnyOrder(product, sideProduct);
                assertThat(data).extracting(InventoryUpdateNotificationMessage.InventoryUpdateData::getRemovedItems).asList()
                        .containsExactlyInAnyOrder(material2Stack1);

            } else if (data.getInventoryID().equals(container2.getInventoryID())) {

                assertThat(data).extracting(InventoryUpdateNotificationMessage.InventoryUpdateData::getAddedItems).asList().isEmpty();
                assertThat(data).extracting(InventoryUpdateNotificationMessage.InventoryUpdateData::getRemovedItems).asList()
                        .containsExactlyInAnyOrder(material2Stack2, material3);

            } else {
                fail();
            }
        }
    }

    @Test
    public void simpleFabricateItemWithoutEnoughItemsTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        IFabrication fabrication = Database.fabricationList.stream().findFirst().orElseThrow();

        BaseMessage message = client.sendMessage(new FabricateItemRequestMessage(playerCharacter.getCharacterID(), fabrication, calender.getTime()));

        assertThat(message).isInstanceOf(NotPossibleMessage.class);
    }

    @Test
    public void fabricateItemWithoutEnoughSpaceTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);
        Container container = assignDefaultInventory(manager, client);
        Container fullContainer = assignDefaultInventory(manager, client, 0, 0);

        Item product = new Item("Product");
        Item sideProduct = new Item("SideProduct");
        Item material1 = new Item("material1");
        Item material2 = new Item("material2");
        material2.setAmount(4);
        Item material3 = new Item("material3");
        Item material4 = new Item("material4");
        material4.setAmount(3);

        Fabrication fabrication = new Fabrication();
        fabrication.setProduct(product);
        fabrication.setSideProductAmount(2);
        fabrication.setSideProduct(sideProduct);
        fabrication.setMaterials(new ItemList(material1, material2, material3, material4));

        // Server accepts only known fabrications
        Database.fabricationList.add(fabrication);

        playerCharacter.getInventory().add(material1);
        playerCharacter.getInventory().add(material2);
        container.getInventory().add(material3);
        container.getInventory().add(material4);

        BaseMessage message = client.sendMessage(
                new FabricateItemRequestMessage(
                        Arrays.asList(playerCharacter.getCharacterID(), container.getInventoryID()),
                        fullContainer.getInventoryID(),
                        fabrication, calender.getTime()
                )
        );

        assertThat(message).isInstanceOf(NotPossibleMessage.class);
        assertThat(playerCharacter.getInventory()).containsExactlyInAnyOrder(material1, material2);
        assertThat(playerCharacter.getInventory()).matches(inv -> ((IInventory) inv).containsAmount(Arrays.asList(material1, material2)));
        assertThat(container.getInventory()).containsExactlyInAnyOrder(material3, material4);
        assertThat(container.getInventory()).matches(inv -> ((IInventory) inv).containsAmount(Arrays.asList(material3, material4)));
    }
}
