package de.pnp.manager.server.database.upgrade;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.AdditiveUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.EUpgradeManipulator;
import de.pnp.manager.component.upgrade.effect.MultiplicativeUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import de.pnp.manager.server.database.RepositoryTestBase;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
                List.of(SimpleUpgradeEffect.create("The weapon emits light")));

        testRepositoryLink(Upgrade::getTarget, typeRepository, upgrade, typeA, typeB);
    }

    @Override
    protected Upgrade createObject() {
        ItemType type = typeRepository.insert(getUniverseName(), new ItemType(null, "Weapon", ETypeRestriction.WEAPON));
        return new Upgrade(null, "Shine", type, 1, 10, List.of(SimpleUpgradeEffect.create("The weapon emits light")));
    }

    @Override
    protected Upgrade createSlightlyChangeObject() {
        ItemType type = typeRepository.insert(getUniverseName(),
                new ItemType(null, "Equipment", ETypeRestriction.EQUIPMENT));
        return new Upgrade(null, "Shine", type, 1, 10,
                List.of(SimpleUpgradeEffect.create("The equipment emits light")));
    }

    @Override
    protected List<Upgrade> createMultipleObjects() {
        ItemType type = typeRepository.insert(getUniverseName(), new ItemType(null, "Item", ETypeRestriction.ITEM));
        return List.of(
                new Upgrade(null, "Shine", type, 1, 10,
                        List.of(new MultiplicativeUpgradeEffect("The weapon emits light", EUpgradeManipulator.HIT, 2))),
                new Upgrade(null, "Fire", type, 2, 70,
                        List.of(new AdditiveUpgradeEffect("The item is on fire", EUpgradeManipulator.DAMAGE, 1))));
    }
}