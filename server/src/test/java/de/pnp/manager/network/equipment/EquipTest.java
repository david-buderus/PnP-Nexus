package de.pnp.manager.network.equipment;

import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.character.data.ArmorPosition;
import de.pnp.manager.model.item.Armor;
import de.pnp.manager.model.item.IEquipment;
import de.pnp.manager.model.item.Jewellery;
import de.pnp.manager.model.item.Weapon;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.equipment.EquipRequestMessage;
import de.pnp.manager.network.message.character.equipment.EquipmentData;
import de.pnp.manager.network.message.character.equipment.EquipmentType;
import de.pnp.manager.network.message.character.equipment.EquipmentUpdateNotificationMessage;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@ResourceLock(value = "SERVER_SOCKET", mode = ResourceAccessMode.READ_WRITE)
@ExtendWith(ApplicationExtension.class)
public class EquipTest extends TestWithManager implements TestClientHelper {

    @Test
    public void simpleEquipWeaponTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Weapon weapon = new Weapon();
        weapon.setName("Sword");

        playerCharacter.getInventory().add(weapon);

        BaseMessage message1 = client.sendMessage(new EquipRequestMessage(new EquipmentData(
                playerCharacter.getCharacterID(),
                playerCharacter.getCharacterID(),
                weapon,
                EquipmentType.WEAPON
        ), calender.getTime()));

        BaseMessage message2 = client.receiveMessage();

        simpleMessageCheck(Arrays.asList(message1, message2), weapon, playerCharacter.getCharacterID());

        assertThat(playerCharacter.getInventory()).isEmpty();
        assertThat(playerCharacter.getWeapons()).contains(weapon);
        assertThat(playerCharacter.getEquippedWeapons()).isEmpty();
    }

    @Test
    public void simpleEquipDirectWeaponTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Weapon weapon = new Weapon();
        weapon.setName("Sword");

        playerCharacter.getInventory().add(weapon);

        BaseMessage message1 = client.sendMessage(new EquipRequestMessage(new EquipmentData(
                playerCharacter.getCharacterID(),
                playerCharacter.getCharacterID(),
                weapon,
                EquipmentType.EQUIPPED_WEAPON
        ), calender.getTime()));

        BaseMessage message2 = client.receiveMessage();

        simpleMessageCheck(Arrays.asList(message1, message2), weapon, playerCharacter.getCharacterID());

        assertThat(playerCharacter.getInventory()).isEmpty();
        assertThat(playerCharacter.getWeapons()).contains(weapon);
        assertThat(playerCharacter.getEquippedWeapons()).contains(weapon);
    }

    @Test
    public void simpleEquipJewelleryTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Jewellery jewellery = new Jewellery();
        jewellery.setName("Ring");
        jewellery.setType("Ring");

        playerCharacter.getInventory().add(jewellery);

        BaseMessage message1 = client.sendMessage(new EquipRequestMessage(new EquipmentData(
                playerCharacter.getCharacterID(),
                playerCharacter.getCharacterID(),
                jewellery,
                EquipmentType.JEWELLERY
        ), calender.getTime()));

        BaseMessage message2 = client.receiveMessage();

        simpleMessageCheck(Arrays.asList(message1, message2), jewellery, playerCharacter.getCharacterID());

        assertThat(playerCharacter.getInventory()).isEmpty();
        assertThat(playerCharacter.getEquippedJewellery()).contains(jewellery);
    }

    @Test
    public void simpleEquipArmorTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Armor armor = new Armor();
        armor.setName("Helmet");
        armor.setSubtype(ArmorPosition.HEAD.toStringProperty().get());

        playerCharacter.getInventory().add(armor);

        BaseMessage message1 = client.sendMessage(new EquipRequestMessage(new EquipmentData(
                playerCharacter.getCharacterID(),
                playerCharacter.getCharacterID(),
                armor,
                EquipmentType.ARMOR
        ), calender.getTime()));

        BaseMessage message2 = client.receiveMessage();

        simpleMessageCheck(Arrays.asList(message1, message2), armor, playerCharacter.getCharacterID());

        assertThat(playerCharacter.getInventory()).isEmpty();
        assertThat(playerCharacter.getEquippedArmor().get(ArmorPosition.HEAD)).isEqualTo(armor);
    }

    protected void simpleMessageCheck(Collection<BaseMessage> messages, IEquipment equipment, String invID) {
        assertThat(messages).anyMatch(message -> message instanceof InventoryUpdateNotificationMessage);
        assertThat(messages).anyMatch(message -> message instanceof EquipmentUpdateNotificationMessage);
        assertThat(messages).hasSize(2);

        for (BaseMessage message : messages) {
            if (message instanceof InventoryUpdateNotificationMessage) {
                final InventoryUpdateNotificationMessage.InventoryUpdateData data =
                        ((InventoryUpdateNotificationMessage) message).getData().stream().findFirst().orElseThrow();

                assertThat(data.getInventoryID()).isEqualTo(invID);
                assertThat(data.getAddedItems()).isEmpty();
                assertThat(data.getRemovedItems()).asList().containsExactlyInAnyOrder(equipment);

            } else if (message instanceof EquipmentUpdateNotificationMessage) {
                final EquipmentUpdateNotificationMessage.EquipmentUpdateData data =
                        ((EquipmentUpdateNotificationMessage) message).getData().stream().findFirst().orElseThrow();

                assertThat(data.getCharacterID()).isEqualTo(invID);
                assertThat(data.getAddedItems()).asList().containsExactlyInAnyOrder(equipment);
                assertThat(data.getRemovedItems()).isEmpty();
            } else {
                fail();
            }
        }
    }
}
