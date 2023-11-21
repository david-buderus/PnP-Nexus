package de.pnp.manager.server.contoller;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.inventory.equipment.DefensiveEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.universe.CurrencyCalculation;
import de.pnp.manager.component.universe.UniverseSettings;
import de.pnp.manager.server.UniverseTestBase;
import de.pnp.manager.utils.TestItemBuilder.TestItemBuilderFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        updateUniverseSettings(new UniverseSettings(10, new CurrencyCalculation("Copper", "C", List.of())));

        WeaponEquipment equipment = new WeaponEquipment(1,
            itemBuilder.createItemBuilder(getUniverseName()).withDamage(3).buildWeapon(), 0);

        assertThat(controller.applyWearByUsage(getUniverseName(), equipment, 4)).isTrue();
        assertThat(equipment.getDamage()).isEqualTo(3);

        assertThat(controller.applyWearByUsage(getUniverseName(), equipment, 6)).isTrue();
        assertThat(equipment.getDamage()).isEqualTo(2);

        assertThat(controller.applyWearByUsage(getUniverseName(), equipment, 20)).isFalse();
        assertThat(equipment.getDamage()).isEqualTo(0);
    }

    @Test
    void testUniverseWithoutWear() {
        updateUniverseSettings(new UniverseSettings(-1, new CurrencyCalculation("Copper", "C", List.of())));

        DefensiveEquipment equipment = new DefensiveEquipment(1,
            itemBuilder.createItemBuilder(getUniverseName()).withArmor(3).buildArmor(), 0);

        assertThat(controller.applyWearByUsage(getUniverseName(), equipment, 40)).isTrue();
        assertThat(equipment.getArmor()).isEqualTo(3);
    }
}