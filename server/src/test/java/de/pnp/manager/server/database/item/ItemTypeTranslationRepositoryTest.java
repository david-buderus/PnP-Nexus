package de.pnp.manager.server.database.item;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.item.ItemTypeTranslation;
import de.pnp.manager.server.database.RepositoryTestBase;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ItemTypeTranslationRepository}.
 */
public class ItemTypeTranslationRepositoryTest extends
    RepositoryTestBase<ItemTypeTranslation, ItemTypeTranslationRepository> {

    @Autowired
    private ItemTypeRepository typeRepository;

    public ItemTypeTranslationRepositoryTest(
        @Autowired ItemTypeTranslationRepository repository) {
        super(repository);
    }

    @Test
    void testAddTypeTranslation() {
        ItemType type = asType("One-handed Sword");
        ItemTypeTranslation typeTranslation = repository.addTypeTranslation(universeName, type,
            Set.of(asType("Weapon"), asType("One-handed weapon")));

        assertThat(typeTranslation.getType()).isEqualTo(type);
        assertThat(typeTranslation.getBroaderVariants()).containsExactlyInAnyOrder(asType("Weapon"),
            asType("One-handed weapon"));

        typeTranslation = repository.addTypeTranslation(universeName, type, asType("Blade"));

        assertThat(typeTranslation.getType()).isEqualTo(type);
        assertThat(typeTranslation.getBroaderVariants()).containsExactlyInAnyOrder(asType("Weapon"),
            asType("One-handed weapon"), asType("Blade"));
    }

    @Test
    void testTransitiveGet() {
        repository.addTypeTranslation(universeName, asType("One-handed sword"),
            Set.of(asType("Sword"), asType("One-handed weapon")));
        repository.addTypeTranslation(universeName, asType("Sword"),
            Set.of(asType("Weapon"), asType("Blade")));
        repository.addTypeTranslation(universeName, asType("One-handed weapon"),
            Set.of(asType("Weapon")));

        assertThat(
            repository.getAllVariants(universeName, asType("One-handed sword"))).containsExactlyInAnyOrder(
            asType("One-handed sword"), asType("Sword"), asType("One-handed weapon"), asType("Weapon"),
            asType("Blade"));
    }

    @Test
    @Timeout(1)
    void testLoopSafety() {
        repository.addTypeTranslation(universeName, asType("A"), Set.of(asType("B")));
        repository.addTypeTranslation(universeName, asType("B"), Set.of(asType("A")));

        assertThat(repository.getAllVariants(universeName, asType("A"))).containsExactlyInAnyOrder(
            asType("A"), asType("B"));
    }

    @Test
    void testTypeLink() {
        ItemType typeA = typeRepository.insert(universeName,
            new ItemType(null, "Type A", ETypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", ETypeRestriction.WEAPON);
        ItemTypeTranslation typeTranslation = new ItemTypeTranslation(null, typeA, Set.of());

        testRepositoryLink(ItemTypeTranslation::getType, typeRepository, typeTranslation, typeA, typeB);
    }

    @Test
    void testVariantsLink() {
        ItemType typeA = typeRepository.insert(universeName,
            new ItemType(null, "Type A", ETypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", ETypeRestriction.WEAPON);
        ItemTypeTranslation typeTranslation = new ItemTypeTranslation(null, asType("test"),
            Set.of(typeA));

        testRepositoryCollectionLink(ItemTypeTranslation::getBroaderVariants, typeRepository,
            typeTranslation, Set.of(typeA), Map.of(typeA, typeB));
    }

    @Override
    protected ItemTypeTranslation createObject() {
        return new ItemTypeTranslation(null, asType("Sword"), Set.of(asType("Weapon")));
    }

    @Override
    protected ItemTypeTranslation createSlightlyChangeObject() {
        return new ItemTypeTranslation(null, asType("Sword"),
            Set.of(asType("Blade"), asType("Weapon")));
    }

    @Override
    protected Collection<ItemTypeTranslation> createMultipleObjects() {
        return List.of(new ItemTypeTranslation(null, asType("Sword"),
            Set.of(asType("Blade"), asType("Weapon"))), new ItemTypeTranslation(null, asType("Blade"),
            Set.of(asType("Weapon"))));
    }

    /**
     * Returns an already persisted {@link ItemType} with the given name.
     */
    protected ItemType asType(String type) {
        return typeRepository.get(universeName, type)
            .orElse(typeRepository.insert(universeName, new ItemType(null, type, ETypeRestriction.ITEM)));
    }
}
