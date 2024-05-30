package de.pnp.manager.server.database.upgrade;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EUpgradeEffectCalculation;
import de.pnp.manager.component.upgrade.effect.EUpgradeEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import de.pnp.manager.server.database.RepositoryTestBase;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link UpgradeRepository}.
 */
class UpgradeRepositoryTest extends RepositoryTestBase<Upgrade, UpgradeRepository> {

    @Autowired
    private ItemTypeRepository typeRepository;

    public UpgradeRepositoryTest(@Autowired UpgradeRepository repository) {
        super(repository);
    }

    @Test
    void testTypeLink() {
        ItemType typeA = typeRepository.insert(getUniverseName(),
            new ItemType(null, "Type A", ETypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", ETypeRestriction.ITEM);
        Upgrade upgrade = new Upgrade(null, "Shine", typeA, 1, 10,
            List.of(new SimpleUpgradeEffect("The weapon emits light")));

        testRepositoryLink(Upgrade::getTarget, typeRepository, upgrade, typeA, typeB);
    }

    @Override
    protected Upgrade createObject() {
        ItemType type = typeRepository.insert(getUniverseName(), new ItemType(null, "Weapon", ETypeRestriction.WEAPON));
        return new Upgrade(null, "Shine", type, 1, 10, List.of(new SimpleUpgradeEffect("The weapon emits light")));
    }

    @Override
    protected Upgrade createSlightlyChangeObject() {
        ItemType type = typeRepository.insert(getUniverseName(),
            new ItemType(null, "Equipment", ETypeRestriction.EQUIPMENT));
        return new Upgrade(null, "Shine", type, 1, 10,
            List.of(new SimpleUpgradeEffect("The equipment emits light")));
    }

    @Override
    protected List<Upgrade> createMultipleObjects() {
        ItemType type = typeRepository.insert(getUniverseName(), new ItemType(null, "Item", ETypeRestriction.ITEM));
        return List.of(
            new Upgrade(null, "Shine", type, 1, 10,
                List.of(
                    new EquipmentUpgradeEffect("The weapon emits light", 2, EUpgradeEquipmentManipulator.HIT,
                        EUpgradeEffectCalculation.MULTIPLICATIVE))),
            new Upgrade(null, "Fire", type, 2, 70,
                List.of(new EquipmentUpgradeEffect("The item is on fire", 1, EUpgradeEquipmentManipulator.DAMAGE,
                    EUpgradeEffectCalculation.ADDITIVE))));
    }
}