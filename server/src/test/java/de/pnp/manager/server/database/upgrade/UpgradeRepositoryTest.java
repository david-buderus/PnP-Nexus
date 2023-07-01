package de.pnp.manager.server.database.upgrade;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.TypeRestriction;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import de.pnp.manager.server.database.RepositoryTestBase;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import java.util.Collection;
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
        ItemType typeA = typeRepository.insert(universeName,
            new ItemType(null, "Type A", TypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", TypeRestriction.ITEM);
        Upgrade upgrade = new Upgrade(null, "Shine", typeA, 1, 10,
            List.of(new SimpleUpgradeEffect("The weapon emits light")));

        testRepositoryLink(Upgrade::getTarget, typeRepository, upgrade, typeA, typeB);
    }

    @Override
    protected Upgrade createObject() {
        ItemType type = typeRepository.insert(universeName, new ItemType(null, "Weapon", TypeRestriction.WEAPON));
        return new Upgrade(null, "Shine", type, 1, 10, List.of(new SimpleUpgradeEffect("The weapon emits light")));
    }

    @Override
    protected Upgrade createSlightlyChangeObject() {
        ItemType type = typeRepository.insert(universeName, new ItemType(null, "Equipment", TypeRestriction.EQUIPMENT));
        return new Upgrade(null, "Shine", type, 1, 10, List.of(new SimpleUpgradeEffect("The equipment emits light")));
    }

    @Override
    protected Collection<Upgrade> createMultipleObjects() {
        ItemType type = typeRepository.insert(universeName, new ItemType(null, "Item", TypeRestriction.ITEM));
        return List.of(
            new Upgrade(null, "Shine", type, 1, 10, List.of(new SimpleUpgradeEffect("The weapon emits light"))),
            new Upgrade(null, "Fire", type, 2, 70, List.of(new SimpleUpgradeEffect("The item is on fire"))));
    }
}