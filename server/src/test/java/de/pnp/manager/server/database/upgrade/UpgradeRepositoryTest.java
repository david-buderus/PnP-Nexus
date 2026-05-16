package de.pnp.manager.server.database.upgrade;

import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.TagRequirement;
import de.pnp.manager.component.upgrade.EUpgradeRestriction;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EItemEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentItemEffect;
import de.pnp.manager.component.upgrade.effect.SimpleItemEffect;
import de.pnp.manager.server.database.RepositoryTestBase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**
 * Tests for {@link UpgradeRepository}.
 */
class UpgradeRepositoryTest extends RepositoryTestBase<Upgrade, UpgradeRepository> {

    public UpgradeRepositoryTest(@Autowired UpgradeRepository repository) {
        super(repository);
    }

    @Override
    protected Upgrade createObject() {
        return new Upgrade(null, "Shine", EUpgradeRestriction.WEAPON, TagRequirement.NO_REQUIREMENT, 1, 10,
                List.of(new SimpleItemEffect("The weapon emits light")));
    }

    @Override
    protected Upgrade createSlightlyChangeObject() {
        return new Upgrade(null, "Shine", EUpgradeRestriction.EQUIPMENT,
                TagRequirement.from(List.of(Set.of("Tag 1"), Set.of("Tag 2", "Tag 3"))), 1, 10,
                List.of(new SimpleItemEffect("The equipment emits light")));
    }

    @Override
    protected List<Upgrade> createMultipleObjects() {
        return List.of(
                new Upgrade(null, "Shine", EUpgradeRestriction.WEAPON, TagRequirement.NO_REQUIREMENT, 1, 10,
                        List.of(
                                new EquipmentItemEffect("The weapon emits light", 2, EItemEquipmentManipulator.HIT,
                                        ECalculation.MULTIPLICATIVE))),
                new Upgrade(null, "Fire", EUpgradeRestriction.WEAPON, TagRequirement.NO_REQUIREMENT, 2, 70,
                        List.of(new EquipmentItemEffect("The item is on fire", 1, EItemEquipmentManipulator.DAMAGE,
                                ECalculation.ADDITIVE))));
    }
}