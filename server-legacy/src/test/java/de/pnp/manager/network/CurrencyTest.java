package de.pnp.manager.network;

import de.pnp.manager.model.Currency;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.currency.*;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.testHelper.TestClient;
import de.pnp.manager.testHelper.TestClientHelper;
import de.pnp.manager.testHelper.TestWithManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ResourceLock(value = "SERVER_SOCKET", mode = ResourceAccessMode.READ_WRITE)
@ExtendWith(ApplicationExtension.class)
public class CurrencyTest extends TestWithManager implements TestClientHelper {

    @Test
    public void createCurrencyTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        final ICurrency currency = new Currency(42);
        final ICurrency toAdd = new Currency(17);
        playerCharacter.setCurrency(currency);

        BaseMessage message = client.sendMessage(new CreateCurrencyRequestMessage(playerCharacter.getCharacterID(), toAdd, calender.getTime()));
        CurrencyData data = ((CurrencyUpdateNotificationMessage) message).getData().stream().findFirst().orElseThrow();

        assertThat(playerCharacter.getCurrency()).isEqualTo(currency.add(toAdd));
        assertThat(playerCharacter.getCharacterID()).isEqualTo(data.getCharacterID());
        assertThat(playerCharacter.getCurrency()).isEqualTo(data.getCurrency());
    }

    @Test
    public void deleteCurrencyTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        final Currency currency = new Currency(42);
        final Currency toDelete = new Currency(39);

        playerCharacter.setCurrency(currency);

        BaseMessage message = client.sendMessage(new DeleteCurrencyRequestMessage(playerCharacter.getCharacterID(), toDelete, calender.getTime()));
        CurrencyData data = ((CurrencyUpdateNotificationMessage) message).getData().stream().findFirst().orElseThrow();

        assertThat(playerCharacter.getCurrency()).isEqualTo(currency.sub(toDelete));
        assertThat(playerCharacter.getCharacterID()).isEqualTo(data.getCharacterID());
        assertThat(playerCharacter.getCurrency()).isEqualTo(data.getCurrency());
    }

    @Test
    public void moveCurrencyTest() throws IOException {
        TestClient client1 = createPreparedClient(manager);
        PlayerCharacter playerCharacter1 = assignDefaultCharacter(manager, client1);
        PlayerCharacter playerCharacter2 = assignDefaultCharacter(manager, client1);

        final Currency currency1 = new Currency(420);
        final Currency currency2 = new Currency(17);
        final Currency toMove = new Currency(42);

        playerCharacter1.setCurrency(currency1);
        playerCharacter2.setCurrency(currency2);

        BaseMessage message = client1.sendMessage(new MoveCurrencyRequestMessage(playerCharacter1.getCharacterID(),
                playerCharacter2.getCharacterID(), toMove, calender.getTime()));
        CurrencyData data1 = ((CurrencyUpdateNotificationMessage) message).getData().stream().findFirst().orElseThrow();
        CurrencyData data2 = ((CurrencyUpdateNotificationMessage) client1.receiveMessage()).getData().stream().findFirst().orElseThrow();

        final List<CurrencyData> data = Arrays.asList(data1, data2);

        assertThat(data).map(CurrencyData::getCharacterID).containsExactlyInAnyOrder(playerCharacter1.getCharacterID(), playerCharacter2.getCharacterID());

        CurrencyData dataForP1 = data.stream().filter(c -> c.getCharacterID().equals(playerCharacter1.getCharacterID())).findFirst().orElseThrow();
        CurrencyData dataForP2 = data.stream().filter(c -> c.getCharacterID().equals(playerCharacter2.getCharacterID())).findFirst().orElseThrow();

        assertThat(playerCharacter1.getCurrency()).isEqualTo(currency1.sub(toMove));
        assertThat(playerCharacter1.getCharacterID()).isEqualTo(dataForP1.getCharacterID());
        assertThat(playerCharacter1.getCurrency()).isEqualTo(dataForP1.getCurrency());

        assertThat(playerCharacter2.getCurrency()).isEqualTo(currency2.add(toMove));
        assertThat(playerCharacter2.getCharacterID()).isEqualTo(dataForP2.getCharacterID());
        assertThat(playerCharacter2.getCurrency()).isEqualTo(dataForP2.getCurrency());
    }

    @Test
    public void moveCurrencyToInventoryTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);
        Container container = assignDefaultInventory(manager, client, 1000, 3);

        final Currency currency = new Currency(66666);
        final Currency toMove = new Currency(42121);

        playerCharacter.setCurrency(currency);

        BaseMessage message1 = client.sendMessage(new MoveCurrencyToInventoryRequestMessage(playerCharacter.getCharacterID(),
                container.getInventoryID(), toMove, calender.getTime()));
        BaseMessage message2 = client.receiveMessage();
        CurrencyData currencyData;
        InventoryUpdateNotificationMessage.InventoryUpdateData inventoryData;

        if (message1 instanceof CurrencyUpdateNotificationMessage) {
            currencyData = ((CurrencyUpdateNotificationMessage) message1).getData().stream().findFirst().orElseThrow();
            inventoryData = ((InventoryUpdateNotificationMessage) message2).getData().stream().findFirst().orElseThrow();
        } else {
            currencyData = ((CurrencyUpdateNotificationMessage) message2).getData().stream().findFirst().orElseThrow();
            inventoryData = ((InventoryUpdateNotificationMessage) message1).getData().stream().findFirst().orElseThrow();
        }

        assertThat(playerCharacter.getCurrency()).isEqualTo(currency.sub(toMove));
        assertThat(playerCharacter.getCharacterID()).isEqualTo(currencyData.getCharacterID());
        assertThat(playerCharacter.getCurrency()).isEqualTo(currencyData.getCurrency());

        assertThat(container.getInventory()).asList().containsExactlyInAnyOrderElementsOf(toMove.toItems());
        assertThat(container.getInventoryID()).isEqualTo(inventoryData.getInventoryID());
        assertThat(container.getInventory()).asList().containsExactlyInAnyOrderElementsOf(inventoryData.getAddedItems());
        assertThat(inventoryData.getRemovedItems()).isEmpty();
    }

    @Test
    public void moveCurrencyFromInventoryTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);
        Container container = assignDefaultInventory(manager, client, 1000, 3);

        final Currency containerCurrency = new Currency(66666);
        final Currency playerCurrency = new Currency(11);
        final Currency toMove = new Currency(42121);
        final Collection<IItem> toMoveItems = toMove.toItems();

        playerCharacter.setCurrency(playerCurrency);
        container.getInventory().addAll(containerCurrency.toItems());

        BaseMessage message1 = client.sendMessage(new MoveCurrencyFromInventoryRequestMessage(
                container.getInventoryID(), playerCharacter.getCharacterID(),toMoveItems, calender.getTime()));
        BaseMessage message2 = client.receiveMessage();
        CurrencyData currencyData;
        InventoryUpdateNotificationMessage.InventoryUpdateData inventoryData;

        if (message1 instanceof CurrencyUpdateNotificationMessage) {
            currencyData = ((CurrencyUpdateNotificationMessage) message1).getData().stream().findFirst().orElseThrow();
            inventoryData = ((InventoryUpdateNotificationMessage) message2).getData().stream().findFirst().orElseThrow();
        } else {
            currencyData = ((CurrencyUpdateNotificationMessage) message2).getData().stream().findFirst().orElseThrow();
            inventoryData = ((InventoryUpdateNotificationMessage) message1).getData().stream().findFirst().orElseThrow();
        }

        assertThat(playerCharacter.getCurrency()).isEqualTo(playerCurrency.add(toMove));
        assertThat(playerCharacter.getCharacterID()).isEqualTo(currencyData.getCharacterID());
        assertThat(playerCharacter.getCurrency()).isEqualTo(currencyData.getCurrency());

        assertThat(container.getInventory()).asList().containsExactlyInAnyOrderElementsOf(containerCurrency.sub(toMove).toItems());
        assertThat(container.getInventoryID()).isEqualTo(inventoryData.getInventoryID());
        assertThat(inventoryData.getRemovedItems()).asList().containsExactlyInAnyOrderElementsOf(toMoveItems);
        assertThat(inventoryData.getAddedItems()).isEmpty();
    }
}
