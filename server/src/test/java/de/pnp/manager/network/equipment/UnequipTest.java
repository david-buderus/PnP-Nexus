package de.pnp.manager.network.equipment;

import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.character.data.ArmorPosition;
import de.pnp.manager.model.item.Armor;
import de.pnp.manager.model.item.IEquipment;
import de.pnp.manager.model.item.Jewellery;
import de.pnp.manager.model.item.Weapon;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.equipment.*;
import de.pnp.manager.network.message.error.ErrorMessage;
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
public class UnequipTest extends TestWithManager implements TestClientHelper {

    @Test
    public void simpleUnequipWeaponTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Weapon weapon = new Weapon();
        weapon.setName("Sword");

        playerCharacter.getWeapons().add(weapon);
        playerCharacter.getEquippedWeapons().add(weapon);

        BaseMessage message1 = client.sendMessage(new UnequipRequestMessage(new EquipmentData(
                playerCharacter.getCharacterID(),
                playerCharacter.getCharacterID(),
                weapon,
                EquipmentType.WEAPON
        ), calender.getTime()));

        simpleMessageCheck(Arrays.asList(message1, client.receiveMessage(), client.receiveMessage()), weapon, playerCharacter.getCharacterID());

        assertThat(playerCharacter.getInventory()).contains(weapon);
        assertThat(playerCharacter.getWeapons()).isEmpty();
        assertThat(playerCharacter.getEquippedWeapons()).isEmpty();
    }

    @Test
    public void simpleUnequipDirectWeaponTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Weapon weapon = new Weapon();
        weapon.setName("Sword");

        playerCharacter.getWeapons().add(weapon);
        playerCharacter.getEquippedWeapons().add(weapon);

        BaseMessage message1 = client.sendMessage(new UnequipRequestMessage(new EquipmentData(
                playerCharacter.getCharacterID(),
                playerCharacter.getCharacterID(),
                weapon,
                EquipmentType.EQUIPPED_WEAPON
        ), calender.getTime()));

        simpleMessageCheck(Arrays.asList(message1, client.receiveMessage(), client.receiveMessage()), weapon, playerCharacter.getCharacterID());

        assertThat(playerCharacter.getInventory()).contains(weapon);
        assertThat(playerCharacter.getWeapons()).isEmpty();
        assertThat(playerCharacter.getEquippedWeapons()).isEmpty();
    }

    @Test
    public void simpleUnequipJewelleryTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Jewellery jewellery = new Jewellery();
        jewellery.setName("Ring");
        jewellery.setType("Ring");

        playerCharacter.getEquippedJewellery().add(jewellery);

        BaseMessage message1 = client.sendMessage(new UnequipRequestMessage(new EquipmentData(
                playerCharacter.getCharacterID(),
                playerCharacter.getCharacterID(),
                jewellery,
                EquipmentType.JEWELLERY
        ), calender.getTime()));

        BaseMessage message2 = client.receiveMessage();

        simpleMessageCheck(Arrays.asList(message1, message2), jewellery, playerCharacter.getCharacterID());

        assertThat(playerCharacter.getInventory()).contains(jewellery);
        assertThat(playerCharacter.getEquippedJewellery()).isEmpty();
    }

    @Test
    public void simpleUnequipArmorTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Armor armor = new Armor();
        armor.setName("Legs");
        armor.setSubtype(ArmorPosition.LEGS.toStringProperty().get());

        playerCharacter.getEquippedArmor().put(ArmorPosition.LEGS, armor);

        BaseMessage message1 = client.sendMessage(new UnequipRequestMessage(new EquipmentData(
                playerCharacter.getCharacterID(),
                playerCharacter.getCharacterID(),
                armor,
                EquipmentType.ARMOR
        ), calender.getTime()));

        BaseMessage message2 = client.receiveMessage();

        simpleMessageCheck(Arrays.asList(message1, message2), armor, playerCharacter.getCharacterID());

        assertThat(playerCharacter.getInventory()).contains(armor);
        assertThat(playerCharacter.getEquippedArmor().get(ArmorPosition.LEGS)).isNull();
    }

    protected void simpleMessageCheck(Collection<BaseMessage> messages, IEquipment equipment, String invID) {
        assertThat(messages).anyMatch(message -> message instanceof InventoryUpdateNotificationMessage);
        assertThat(messages).anyMatch(message -> message instanceof EquipmentUpdateNotificationMessage);

        for (BaseMessage message : messages) {
            if (message instanceof InventoryUpdateNotificationMessage) {
                final InventoryUpdateNotificationMessage.InventoryUpdateData data =
                        ((InventoryUpdateNotificationMessage) message).getData().stream().findFirst().orElseThrow();

                assertThat(data.getInventoryID()).isEqualTo(invID);
                assertThat(data.getAddedItems()).asList().containsExactlyInAnyOrder(equipment);
                assertThat(data.getRemovedItems()).isEmpty();

            } else if (message instanceof EquipmentUpdateNotificationMessage) {
                final EquipmentUpdateNotificationMessage.EquipmentUpdateData data =
                        ((EquipmentUpdateNotificationMessage) message).getData().stream().findFirst().orElseThrow();

                assertThat(data.getCharacterID()).isEqualTo(invID);
                assertThat(data.getAddedItems()).isEmpty();
                assertThat(data.getRemovedItems()).asList().containsExactlyInAnyOrder(equipment);
            } else {
                fail();
            }
        }
    }
}
