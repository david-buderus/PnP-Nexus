package de.pnp.manager.server.database.upgrade;

import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.TagRequirement;
import de.pnp.manager.component.upgrade.EUpgradeRestriction;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EUpgradeEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import de.pnp.manager.server.database.RepositoryTestBase;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

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
            List.of(new SimpleUpgradeEffect("The weapon emits light")));
    }

    @Override
    protected Upgrade createSlightlyChangeObject() {
        return new Upgrade(null, "Shine", EUpgradeRestriction.EQUIPMENT,
            TagRequirement.from(List.of(Set.of("Tag 1"), Set.of("Tag 2", "Tag 3"))), 1, 10,
            List.of(new SimpleUpgradeEffect("The equipment emits light")));
    }

    @Override
    protected List<Upgrade> createMultipleObjects() {
        return List.of(
            new Upgrade(null, "Shine", EUpgradeRestriction.WEAPON, TagRequirement.NO_REQUIREMENT, 1, 10,
                List.of(
                    new EquipmentUpgradeEffect("The weapon emits light", 2, EUpgradeEquipmentManipulator.HIT,
                        ECalculation.MULTIPLICATIVE))),
            new Upgrade(null, "Fire", EUpgradeRestriction.WEAPON, TagRequirement.NO_REQUIREMENT, 2, 70,
                List.of(new EquipmentUpgradeEffect("The item is on fire", 1, EUpgradeEquipmentManipulator.DAMAGE,
                    ECalculation.ADDITIVE))));
    }
}