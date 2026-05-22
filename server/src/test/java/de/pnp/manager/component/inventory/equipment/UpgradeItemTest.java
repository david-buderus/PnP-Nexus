package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.Item;
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
 * Tests for {@link Upgrade upgrades} on {@link ItemStack}.
 */
class UpgradeItemTest {

    private static final Item TEST_ITEM = createItemBuilder().withUpgradeSlots(2).buildItem();

    @Test
    void testAddUpgrades() {
        ItemStack<Item> itemStack = new ItemStack<>(1, TEST_ITEM);
        Upgrade upgradeA = createUpgrade().withSlots(1).addEffect(new SimpleItemEffect("Description")).build();
        itemStack.addUpgrade(upgradeA);
        assertThat(itemStack.getUpgrades()).containsExactly(upgradeA);

        Upgrade upgradeB = createUpgrade().withSlots(2).addEffect(new SimpleItemEffect("Description")).build();
        assertThatThrownBy(() -> itemStack.addUpgrade(upgradeB), "The equipment can not contain so many upgrades.");
    }

    @Test
    void testSetUpgrades() {
        ItemStack<Item> itemStack = new ItemStack<>(1, TEST_ITEM);
        Upgrade upgradeA = createUpgrade().withSlots(1).addEffect(new SimpleItemEffect("Description")).build();
        itemStack.setUpgrades(List.of(upgradeA, upgradeA));
        assertThat(itemStack.getUpgrades()).containsExactly(upgradeA, upgradeA);

        Upgrade upgradeB = createUpgrade().withSlots(2).addEffect(new SimpleItemEffect("Description")).build();
        assertThatThrownBy(() -> itemStack.setUpgrades(List.of(upgradeA, upgradeB)),
                "The equipment can not contain so many upgrades.");
    }

    @Test
    void testUpgradeSlots() {
        ItemStack<Item> itemStack = new ItemStack<>(1, TEST_ITEM);
        assertThat(itemStack.getUpgradeSlots()).isEqualTo(2);
        assertThat(itemStack.getUpgradeSlots()).isEqualTo(2);

        itemStack.addUpgrade(
                createUpgrade().withSlots(1).addEffect(new EquipmentItemEffect("", 2, EItemEquipmentManipulator.SLOTS,
                                ECalculation.ADDITIVE))
                        .build());
        assertThat(itemStack.getUpgradeSlots()).isEqualTo(4);
        assertThat(itemStack.getRemainingUpgradeSlots()).isEqualTo(3);
    }

    @Test
    void testUpgradeEffectOrder() {
        ItemStack<Item> itemStack = new ItemStack<>(1, TEST_ITEM);

        itemStack.addUpgrade(
                createUpgrade().withSlots(0).addEffect(new EquipmentItemEffect("", 2, EItemEquipmentManipulator.SLOTS,
                                ECalculation.ADDITIVE))
                        .build());
        itemStack.addUpgrade(
                createUpgrade().withSlots(0)
                        .addEffect(new EquipmentItemEffect("", 2, EItemEquipmentManipulator.SLOTS,
                                ECalculation.MULTIPLICATIVE))
                        .build());
        itemStack.addUpgrade(
                createUpgrade().withSlots(0).addEffect(new EquipmentItemEffect("", 2, EItemEquipmentManipulator.SLOTS,
                                ECalculation.ADDITIVE))
                        .build());

        assertThat(itemStack.getUpgradeSlots()).isEqualTo(12);
    }
}