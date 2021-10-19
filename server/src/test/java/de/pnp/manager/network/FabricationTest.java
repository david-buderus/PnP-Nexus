package de.pnp.manager.network;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.IFabrication;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.item.Item;
import de.pnp.manager.model.manager.Manager;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void fabricateItemTest() throws IOException {
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
        assertEquals(data.getRemovedItems(), fabrication.getMaterials());
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
