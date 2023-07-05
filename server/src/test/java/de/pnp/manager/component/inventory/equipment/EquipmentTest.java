package de.pnp.manager.component.inventory.equipment;

import static de.pnp.manager.server.database.TestItemBuilder.createItemBuilder;
import static de.pnp.manager.utils.TestUpgradeBuilder.createUpgrade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.AdditiveUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.EUpgradeManipulator;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Equipment}.
 */
class EquipmentTest {

    private static final EquipableItem TEST_ITEM = createItemBuilder().withUpgradeSlots(2).buildJewellery();

    @Test
    void testAddUpgrades() {
        Equipment<EquipableItem> equipment = new Equipment<>(1, TEST_ITEM);
        Upgrade upgradeA = createUpgrade().withSlots(1).addEffect(SimpleUpgradeEffect.create("Description")).build();
        equipment.addUpgrade(upgradeA);
        assertThat(equipment.getUpgrades()).containsExactly(upgradeA);

        Upgrade upgradeB = createUpgrade().withSlots(2).addEffect(SimpleUpgradeEffect.create("Description")).build();
        assertThatThrownBy(() -> equipment.addUpgrade(upgradeB), "The equipment can not contain so many upgrades.");
    }

    @Test
    void testSetUpgrades() {
        Equipment<EquipableItem> equipment = new Equipment<>(1, TEST_ITEM);
        Upgrade upgradeA = createUpgrade().withSlots(1).addEffect(SimpleUpgradeEffect.create("Description")).build();
        equipment.setUpgrades(List.of(upgradeA, upgradeA));
        assertThat(equipment.getUpgrades()).containsExactly(upgradeA, upgradeA);

        Upgrade upgradeB = createUpgrade().withSlots(2).addEffect(SimpleUpgradeEffect.create("Description")).build();
        assertThatThrownBy(() -> equipment.setUpgrades(List.of(upgradeA, upgradeB)),
            "The equipment can not contain so many upgrades.");
    }

    @Test
    void testUpgradeSlots() {
        Equipment<EquipableItem> equipment = new Equipment<>(1, TEST_ITEM);
        assertThat(equipment.getUpgradeSlots()).isEqualTo(2);
        assertThat(equipment.getUpgradeSlots()).isEqualTo(2);

        equipment.addUpgrade(
            createUpgrade().withSlots(1).addEffect(new AdditiveUpgradeEffect("", EUpgradeManipulator.SLOTS, 2))
                .build());
        assertThat(equipment.getUpgradeSlots()).isEqualTo(4);
        assertThat(equipment.getRemainingUpgradeSlots()).isEqualTo(3);
    }
}