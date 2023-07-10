package de.pnp.manager.component.inventory.equipment;

import static de.pnp.manager.server.database.TestItemBuilder.createItemBuilder;
import static de.pnp.manager.utils.TestUpgradeBuilder.createUpgrade;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.component.upgrade.effect.EUpgradeManipulator;
import de.pnp.manager.component.upgrade.effect.MultiplicativeUpgradeEffect;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WeaponEquipment}.
 */
class WeaponEquipmentTest {

    private static final Weapon TEST_WEAPON = createItemBuilder().withDamage(5).withHit(2).withUpgradeSlots(2)
        .buildWeapon();

    @Test
    void testGetDamage() {
        WeaponEquipment weapon = new WeaponEquipment(1, TEST_WEAPON, 0);
        assertThat(weapon.getDamage()).isEqualTo(5);

        weapon.addUpgrade(
            createUpgrade().addEffect(new MultiplicativeUpgradeEffect("", EUpgradeManipulator.DAMAGE, 2)).build());
        assertThat(weapon.getDamage()).isEqualTo(10);

        weapon.applyWear(1);
        assertThat(weapon.getDamage()).isEqualTo(9);

        weapon.applyWear(1.6f);
        assertThat(weapon.getDamage()).isEqualTo(8);
    }
}