package de.pnp.manager.component.inventory.equipment;

import static de.pnp.manager.server.database.TestItemBuilder.createItemBuilder;
import static de.pnp.manager.utils.TestUpgradeBuilder.createUpgrade;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.upgrade.effect.AdditiveUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.EUpgradeManipulator;
import de.pnp.manager.component.upgrade.effect.MultiplicativeUpgradeEffect;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefensiveEquipment}.
 */
class DefensiveEquipmentTest {

    private static final Shield TEST_SHIELD = createItemBuilder().withArmor(5).withHit(2).withInitiative(1)
        .withWeight(3).withUpgradeSlots(2).buildShield();

    @Test
    void testGetArmor() {
        DefensiveEquipment defensiveEquipment = new DefensiveEquipment(1, TEST_SHIELD, 0);
        assertThat(defensiveEquipment.getArmor()).isEqualTo(5);

        defensiveEquipment.addUpgrade(
            createUpgrade().addEffect(new MultiplicativeUpgradeEffect("", EUpgradeManipulator.ARMOR, 2)).build());
        assertThat(defensiveEquipment.getArmor()).isEqualTo(10);

        defensiveEquipment.applyWear(1);
        assertThat(defensiveEquipment.getArmor()).isEqualTo(9);

        defensiveEquipment.applyWear(1.6F);
        assertThat(defensiveEquipment.getArmor()).isEqualTo(8);

        defensiveEquipment.repair();
        assertThat(defensiveEquipment.getArmor()).isEqualTo(10);
    }

    @Test
    void testGetHit() {
        DefensiveEquipment defensiveEquipment = new DefensiveEquipment(1, TEST_SHIELD, 0);
        assertThat(defensiveEquipment.getHit()).isEqualTo(2);

        defensiveEquipment.addUpgrade(
            createUpgrade().addEffect(new AdditiveUpgradeEffect("", EUpgradeManipulator.HIT, 1)).build());
        assertThat(defensiveEquipment.getHit()).isEqualTo(3);
    }

    @Test
    void testGetInitiative() {
        DefensiveEquipment defensiveEquipment = new DefensiveEquipment(1, TEST_SHIELD, 0);
        assertThat(defensiveEquipment.getInitiative()).isEqualTo(1);

        defensiveEquipment.addUpgrade(
            createUpgrade().addEffect(new AdditiveUpgradeEffect("", EUpgradeManipulator.INITIATIVE, 0.5F)).build());
        assertThat(defensiveEquipment.getInitiative()).isEqualTo(1.5F);
    }

    @Test
    void testGetWeight() {
        DefensiveEquipment defensiveEquipment = new DefensiveEquipment(1, TEST_SHIELD, 0);
        assertThat(defensiveEquipment.getWeight()).isEqualTo(3);

        defensiveEquipment.addUpgrade(
            createUpgrade().addEffect(new AdditiveUpgradeEffect("", EUpgradeManipulator.WEIGHT, 2)).build());
        assertThat(defensiveEquipment.getWeight()).isEqualTo(5);
    }
}