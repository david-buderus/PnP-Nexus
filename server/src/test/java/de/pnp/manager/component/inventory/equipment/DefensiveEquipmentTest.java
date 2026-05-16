package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.upgrade.effect.EItemEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentItemEffect;
import org.junit.jupiter.api.Test;

import static de.pnp.manager.utils.TestItemBuilder.createItemBuilder;
import static de.pnp.manager.utils.TestUpgradeBuilder.createUpgrade;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefensiveEquipment}.
 */
class DefensiveEquipmentTest {

    private static final Shield TEST_SHIELD = createItemBuilder().withArmor(5).withHit(2).withInitiative(1)
            .withWeight(3).withUpgradeSlots(2).buildShield();

    @Test
    void testGetArmor() {
        ShieldEquipment defensiveEquipment = new ShieldEquipment(1, TEST_SHIELD, 0);
        assertThat(defensiveEquipment.getArmor()).isEqualTo(5);
        assertThat(defensiveEquipment.getMaxArmor()).isEqualTo(5);

        defensiveEquipment.addUpgrade(
                createUpgrade().addEffect(new EquipmentItemEffect("", 2, EItemEquipmentManipulator.ARMOR,
                                ECalculation.MULTIPLICATIVE))
                        .build());
        assertThat(defensiveEquipment.getArmor()).isEqualTo(10);
        assertThat(defensiveEquipment.getMaxArmor()).isEqualTo(10);

        // Check no other stat got changed due to the upgrade
        assertThat(defensiveEquipment.getHit()).isEqualTo(2);
        assertThat(defensiveEquipment.getInitiative()).isEqualTo(1);
        assertThat(defensiveEquipment.getWeight()).isEqualTo(3);

        defensiveEquipment.applyWear(1);
        assertThat(defensiveEquipment.getArmor()).isEqualTo(9);
        assertThat(defensiveEquipment.getMaxArmor()).isEqualTo(10);

        defensiveEquipment.applyWear(1.6F);
        assertThat(defensiveEquipment.getArmor()).isEqualTo(8);
        assertThat(defensiveEquipment.getMaxArmor()).isEqualTo(10);

        defensiveEquipment.repair();
        assertThat(defensiveEquipment.getArmor()).isEqualTo(10);
        assertThat(defensiveEquipment.getMaxArmor()).isEqualTo(10);
    }

    @Test
    void testGetHit() {
        ShieldEquipment defensiveEquipment = new ShieldEquipment(1, TEST_SHIELD, 0);
        assertThat(defensiveEquipment.getHit()).isEqualTo(2);

        defensiveEquipment.addUpgrade(
                createUpgrade().addEffect(
                                new EquipmentItemEffect("", 1, EItemEquipmentManipulator.HIT, ECalculation.ADDITIVE))
                        .build());
        assertThat(defensiveEquipment.getHit()).isEqualTo(3);

        // Check no other stat got changed due to the upgrade
        assertThat(defensiveEquipment.getArmor()).isEqualTo(5);
        assertThat(defensiveEquipment.getInitiative()).isEqualTo(1);
        assertThat(defensiveEquipment.getWeight()).isEqualTo(3);
    }

    @Test
    void testGetInitiative() {
        ShieldEquipment defensiveEquipment = new ShieldEquipment(1, TEST_SHIELD, 0);
        assertThat(defensiveEquipment.getInitiative()).isEqualTo(1);

        defensiveEquipment.addUpgrade(
                createUpgrade().addEffect(new EquipmentItemEffect("", 0.5F, EItemEquipmentManipulator.INITIATIVE,
                                ECalculation.ADDITIVE))
                        .build());
        assertThat(defensiveEquipment.getInitiative()).isEqualTo(1.5F);

        // Check no other stat got changed due to the upgrade
        assertThat(defensiveEquipment.getArmor()).isEqualTo(5);
        assertThat(defensiveEquipment.getHit()).isEqualTo(2);
        assertThat(defensiveEquipment.getWeight()).isEqualTo(3);
    }

    @Test
    void testGetWeight() {
        ShieldEquipment defensiveEquipment = new ShieldEquipment(1, TEST_SHIELD, 0);
        assertThat(defensiveEquipment.getWeight()).isEqualTo(3);

        defensiveEquipment.addUpgrade(
                createUpgrade().addEffect(new EquipmentItemEffect("", 2, EItemEquipmentManipulator.WEIGHT,
                        ECalculation.ADDITIVE)).build());
        assertThat(defensiveEquipment.getWeight()).isEqualTo(5);

        // Check no other stat got changed due to the upgrade
        assertThat(defensiveEquipment.getArmor()).isEqualTo(5);
        assertThat(defensiveEquipment.getHit()).isEqualTo(2);
        assertThat(defensiveEquipment.getInitiative()).isEqualTo(1);
    }

    @Test
    void testCurseUpgrades() {
        ShieldEquipment defensiveEquipment = new ShieldEquipment(1, TEST_SHIELD, 0);
        assertThat(defensiveEquipment.getArmor()).isEqualTo(5);

        defensiveEquipment.addUpgrade(
                createUpgrade().addEffect(new EquipmentItemEffect("", -3, EItemEquipmentManipulator.ARMOR,
                        ECalculation.ADDITIVE)).build());
        assertThat(defensiveEquipment.getArmor()).isEqualTo(2);

        defensiveEquipment.addUpgrade(
                createUpgrade().addEffect(new EquipmentItemEffect("", -3, EItemEquipmentManipulator.ARMOR,
                        ECalculation.ADDITIVE)).build());
        assertThat(defensiveEquipment.getArmor()).isEqualTo(0);
    }
}