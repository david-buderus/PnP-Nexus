package de.pnp.manager.component.inventory.equipment;

import static de.pnp.manager.utils.TestItemBuilder.createItemBuilder;
import static de.pnp.manager.utils.TestUpgradeBuilder.createUpgrade;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.component.upgrade.effect.EUpgradeEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentUpgradeEffect;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WeaponEquipment}.
 */
class WeaponEquipmentTest {

    private static final Weapon TEST_WEAPON = createItemBuilder().withDamage(5).withHit(2).withInitiative(1)
        .withUpgradeSlots(2).buildWeapon();

    @Test
    void testGetDamage() {
        WeaponEquipment weapon = new WeaponEquipment(1, TEST_WEAPON, 0);
        assertThat(weapon.getDamage()).isEqualTo(5);
        assertThat(weapon.getMaxDamage()).isEqualTo(5);

        weapon.addUpgrade(
            createUpgrade().addEffect(new EquipmentUpgradeEffect("", 2, EUpgradeEquipmentManipulator.DAMAGE,
                    ECalculation.MULTIPLICATIVE))
                .build());
        assertThat(weapon.getDamage()).isEqualTo(10);
        assertThat(weapon.getMaxDamage()).isEqualTo(10);

        // Check no other stat got changed due to the upgrade
        assertThat(weapon.getInitiative()).isEqualTo(1);
        assertThat(weapon.getHit()).isEqualTo(2);

        weapon.applyWear(1);
        assertThat(weapon.getDamage()).isEqualTo(9);
        assertThat(weapon.getMaxDamage()).isEqualTo(10);

        weapon.applyWear(1.6F);
        assertThat(weapon.getDamage()).isEqualTo(8);
        assertThat(weapon.getMaxDamage()).isEqualTo(10);

        weapon.repair();
        assertThat(weapon.getDamage()).isEqualTo(10);
        assertThat(weapon.getMaxDamage()).isEqualTo(10);
    }

    @Test
    void testGetHit() {
        WeaponEquipment weapon = new WeaponEquipment(1, TEST_WEAPON, 0);
        assertThat(weapon.getHit()).isEqualTo(2);

        weapon.addUpgrade(
            createUpgrade().addEffect(
                    new EquipmentUpgradeEffect("", 1, EUpgradeEquipmentManipulator.HIT, ECalculation.ADDITIVE))
                .build());
        assertThat(weapon.getHit()).isEqualTo(3);

        // Check no other stat got changed due to the upgrade
        assertThat(weapon.getInitiative()).isEqualTo(1);
        assertThat(weapon.getDamage()).isEqualTo(5);
    }

    @Test
    void testGetInitiative() {
        WeaponEquipment weapon = new WeaponEquipment(1, TEST_WEAPON, 0);
        assertThat(weapon.getInitiative()).isEqualTo(1);

        weapon.addUpgrade(
            createUpgrade().addEffect(new EquipmentUpgradeEffect("", 0.5F, EUpgradeEquipmentManipulator.INITIATIVE,
                    ECalculation.ADDITIVE))
                .build());
        assertThat(weapon.getInitiative()).isEqualTo(1.5F);

        // Check no other stat got changed due to the upgrade
        assertThat(weapon.getDamage()).isEqualTo(5);
        assertThat(weapon.getHit()).isEqualTo(2);
    }

    @Test
    void testCurseUpgrades() {
        WeaponEquipment weapon = new WeaponEquipment(1, TEST_WEAPON, 0);
        assertThat(weapon.getDamage()).isEqualTo(5);

        weapon.addUpgrade(
            createUpgrade().addEffect(new EquipmentUpgradeEffect("", -3, EUpgradeEquipmentManipulator.DAMAGE,
                ECalculation.ADDITIVE)).build());
        assertThat(weapon.getDamage()).isEqualTo(2);

        weapon.addUpgrade(
            createUpgrade().addEffect(new EquipmentUpgradeEffect("", -3, EUpgradeEquipmentManipulator.DAMAGE,
                ECalculation.ADDITIVE)).build());
        assertThat(weapon.getDamage()).isEqualTo(0);
    }
}