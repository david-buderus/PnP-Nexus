package de.pnp.manager.server.contoller;

import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.universe.ItemSettings;
import de.pnp.manager.server.UniverseTestBase;
import de.pnp.manager.utils.TestItemBuilder.TestItemBuilderFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ItemStackController}.
 */
class ItemStackControllerTest extends UniverseTestBase {

    @Autowired
    private TestItemBuilderFactory itemBuilder;

    @Autowired
    private ItemStackController controller;

    @Test
    void testApplyWearByUsage() {
        updateUniverseSettings(new ItemSettings(10, false, false));

        WeaponEquipment equipment = new WeaponEquipment(1,
                itemBuilder.createItemBuilder(getUniverseId()).withDamage(3).buildWeapon(), 0);

        assertThat(controller.applyWearByUsage(getUniverseId(), equipment, 4)).isTrue();
        assertThat(equipment.getDamage()).isEqualTo(3);

        assertThat(controller.applyWearByUsage(getUniverseId(), equipment, 6)).isTrue();
        assertThat(equipment.getDamage()).isEqualTo(2);

        assertThat(controller.applyWearByUsage(getUniverseId(), equipment, 20)).isFalse();
        assertThat(equipment.getDamage()).isEqualTo(0);
    }

    @Test
    void testUniverseWithoutWear() {
        updateUniverseSettings(new ItemSettings(-1, false, false));

        ArmorEquipment equipment = new ArmorEquipment(1,
                itemBuilder.createItemBuilder(getUniverseId()).withArmor(3).buildArmor(), 0);

        assertThat(controller.applyWearByUsage(getUniverseId(), equipment, 40)).isTrue();
        assertThat(equipment.getArmor()).isEqualTo(3);
    }
}