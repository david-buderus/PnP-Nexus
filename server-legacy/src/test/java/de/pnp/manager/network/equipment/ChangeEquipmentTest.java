package de.pnp.manager.network.equipment;

import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.item.Weapon;
import de.pnp.manager.network.message.character.equipment.ChangeEquippedWeaponsRequestMessage;
import de.pnp.manager.network.message.character.equipment.EquipmentUpdateNotificationMessage;
import de.pnp.manager.testHelper.TestClient;
import de.pnp.manager.testHelper.TestClientHelper;
import de.pnp.manager.testHelper.TestWithManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ResourceLock(value = "SERVER_SOCKET", mode = ResourceAccessMode.READ_WRITE)
@ExtendWith(ApplicationExtension.class)
public class ChangeEquipmentTest extends TestWithManager implements TestClientHelper {

    @Test
    public void drawWeaponTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Weapon sword = new Weapon();
        sword.setName("Sword");

        playerCharacter.getWeapons().add(sword);

        EquipmentUpdateNotificationMessage message = (EquipmentUpdateNotificationMessage) client.sendMessage(new ChangeEquippedWeaponsRequestMessage(
                playerCharacter.getCharacterID(),
                Collections.singleton(sword),
                Collections.emptyList(),
                calender.getTime())
        );

        final EquipmentUpdateNotificationMessage.EquipmentUpdateData data = message.getData().stream().findFirst().orElseThrow();

        assertThat(data.getCharacterID()).isEqualTo(playerCharacter.getCharacterID());
        assertThat(data.getAddedItems()).asList().containsExactlyInAnyOrder(sword);
        assertThat(data.getRemovedItems()).isEmpty();

        assertThat(playerCharacter.getWeapons()).containsExactlyInAnyOrder(sword);
        assertThat(playerCharacter.getEquippedWeapons()).containsExactlyInAnyOrder(sword);
    }

    @Test
    public void putAwayWeaponTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Weapon sword = new Weapon();
        sword.setName("Sword");

        playerCharacter.getWeapons().add(sword);
        playerCharacter.getEquippedWeapons().add(sword);

        EquipmentUpdateNotificationMessage message = (EquipmentUpdateNotificationMessage) client.sendMessage(new ChangeEquippedWeaponsRequestMessage(
                playerCharacter.getCharacterID(),
                Collections.emptyList(),
                Collections.singleton(sword),
                calender.getTime())
        );

        final EquipmentUpdateNotificationMessage.EquipmentUpdateData data = message.getData().stream().findFirst().orElseThrow();

        assertThat(data.getCharacterID()).isEqualTo(playerCharacter.getCharacterID());
        assertThat(data.getAddedItems()).isEmpty();
        assertThat(data.getRemovedItems()).asList().containsExactlyInAnyOrder(sword);

        assertThat(playerCharacter.getWeapons()).containsExactlyInAnyOrder(sword);
        assertThat(playerCharacter.getEquippedWeapons()).isEmpty();
    }

    @Test
    public void swapWeaponTest() throws IOException {
        TestClient client = createPreparedClient(manager);
        PlayerCharacter playerCharacter = assignDefaultCharacter(manager, client);

        Weapon sword = new Weapon();
        sword.setName("Sword");

        Weapon dagger = new Weapon();
        dagger.setName("Dagger");

        playerCharacter.getWeapons().add(sword);
        playerCharacter.getWeapons().add(dagger);
        playerCharacter.getEquippedWeapons().add(sword);

        EquipmentUpdateNotificationMessage message = (EquipmentUpdateNotificationMessage) client.sendMessage(new ChangeEquippedWeaponsRequestMessage(
                playerCharacter.getCharacterID(),
                Collections.singleton(dagger),
                Collections.singleton(sword),
                calender.getTime())
        );

        final EquipmentUpdateNotificationMessage.EquipmentUpdateData data = message.getData().stream().findFirst().orElseThrow();

        assertThat(data.getCharacterID()).isEqualTo(playerCharacter.getCharacterID());
        assertThat(data.getAddedItems()).asList().containsExactlyInAnyOrder(dagger);
        assertThat(data.getRemovedItems()).asList().containsExactlyInAnyOrder(sword);

        assertThat(playerCharacter.getWeapons()).containsExactlyInAnyOrder(sword, dagger);
        assertThat(playerCharacter.getEquippedWeapons()).containsExactlyInAnyOrder(dagger);
    }
}
