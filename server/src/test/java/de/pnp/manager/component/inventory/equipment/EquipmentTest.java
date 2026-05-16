package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EItemEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentItemEffect;
import de.pnp.manager.component.upgrade.effect.SimpleItemEffect;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.pnp.manager.utils.TestItemBuilder.createItemBuilder;
import static de.pnp.manager.utils.TestUpgradeBuilder.createUpgrade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link Equipment}.
 */
class EquipmentTest {

    private static final EquipableItem TEST_ITEM = createItemBuilder().withUpgradeSlots(2).buildJewellery();

    @Test
    void testAddUpgrades() {
        Equipment<EquipableItem> equipment = new Equipment<>(1, TEST_ITEM);
        Upgrade upgradeA = createUpgrade().withSlots(1).addEffect(new SimpleItemEffect("Description")).build();
        equipment.addUpgrade(upgradeA);
        assertThat(equipment.getUpgrades()).containsExactly(upgradeA);

        Upgrade upgradeB = createUpgrade().withSlots(2).addEffect(new SimpleItemEffect("Description")).build();
        assertThatThrownBy(() -> equipment.addUpgrade(upgradeB), "The equipment can not contain so many upgrades.");
    }

    @Test
    void testSetUpgrades() {
        Equipment<EquipableItem> equipment = new Equipment<>(1, TEST_ITEM);
        Upgrade upgradeA = createUpgrade().withSlots(1).addEffect(new SimpleItemEffect("Description")).build();
        equipment.setUpgrades(List.of(upgradeA, upgradeA));
        assertThat(equipment.getUpgrades()).containsExactly(upgradeA, upgradeA);

        Upgrade upgradeB = createUpgrade().withSlots(2).addEffect(new SimpleItemEffect("Description")).build();
        assertThatThrownBy(() -> equipment.setUpgrades(List.of(upgradeA, upgradeB)),
                "The equipment can not contain so many upgrades.");
    }

    @Test
    void testUpgradeSlots() {
        Equipment<EquipableItem> equipment = new Equipment<>(1, TEST_ITEM);
        assertThat(equipment.getUpgradeSlots()).isEqualTo(2);
        assertThat(equipment.getUpgradeSlots()).isEqualTo(2);

        equipment.addUpgrade(
                createUpgrade().withSlots(1).addEffect(new EquipmentItemEffect("", 2, EItemEquipmentManipulator.SLOTS,
                                ECalculation.ADDITIVE))
                        .build());
        assertThat(equipment.getUpgradeSlots()).isEqualTo(4);
        assertThat(equipment.getRemainingUpgradeSlots()).isEqualTo(3);
    }

    @Test
    void testUpgradeEffectOrder() {
        Equipment<EquipableItem> equipment = new Equipment<>(1, TEST_ITEM);

        equipment.addUpgrade(
                createUpgrade().withSlots(0).addEffect(new EquipmentItemEffect("", 2, EItemEquipmentManipulator.SLOTS,
                                ECalculation.ADDITIVE))
                        .build());
        equipment.addUpgrade(
                createUpgrade().withSlots(0)
                        .addEffect(new EquipmentItemEffect("", 2, EItemEquipmentManipulator.SLOTS,
                                ECalculation.MULTIPLICATIVE))
                        .build());
        equipment.addUpgrade(
                createUpgrade().withSlots(0).addEffect(new EquipmentItemEffect("", 2, EItemEquipmentManipulator.SLOTS,
                                ECalculation.ADDITIVE))
                        .build());

        assertThat(equipment.getUpgradeSlots()).isEqualTo(12);
    }
}