package de.pnp.manager.server.database.item;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.TypeRestriction;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.RepositoryTestBase;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ItemRepository}.
 */
public class ItemRepositoryTest extends RepositoryTestBase<Item, ItemRepository> {

    @Autowired
    private ItemTypeRepository typeRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ItemTypeTranslationRepository typeTranslationRepository;

    public ItemRepositoryTest(@Autowired ItemRepository repository) {
        super(repository);
    }

    @Test
    void testInsertArmor() {
        Armor armor = itemBuilder.createItemBuilder(universeName).buildArmor();
        Item persistedArmor = repository.insert(universeName, armor);

        assertThat(repository.getAll(universeName)).contains(armor);
        assertThat(repository.get(universeName, armor.getName())).contains(armor);
        assertThat(repository.get(universeName, persistedArmor.getId())).contains(armor);
    }

    @Test
    void testTypeLink() {
        ItemType typeA = typeRepository.insert(universeName,
            new ItemType(null, "Type A", TypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", TypeRestriction.ITEM);
        Item item = itemBuilder.createItemBuilder(universeName).withType(typeA).buildItem();

        testRepositoryLink(Item::getType, typeRepository, item, typeA, typeB);
    }

    @Test
    void testSubtypeLink() {
        ItemType typeA = typeRepository.insert(universeName,
            new ItemType(null, "Type A", TypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", TypeRestriction.ITEM);
        Item item = itemBuilder.createItemBuilder(universeName).withSubtype(typeA).buildItem();

        testRepositoryLink(Item::getSubtype, typeRepository, item, typeA, typeB);
    }

    @Test
    void testMaterialLink() {
        Material materialA = materialRepository.insert(universeName,
            new Material(null, "Material A", Collections.emptyList()));
        Material materialB = new Material(null, "Material B", Collections.emptyList());
        Weapon weapon = itemBuilder.createItemBuilder(universeName).withName("Test").withMaterial(materialA)
            .buildWeapon();

        testRepositoryLink(Weapon::getMaterial, materialRepository, weapon, materialA, materialB);
    }


    @Test
    void testAutomaticTypeTranslations() {
        ItemType type = typeRepository.insert(universeName,
            new ItemType(null, "Type", TypeRestriction.ITEM));
        ItemType subtype = typeRepository.insert(universeName,
            new ItemType(null, "Subtype", TypeRestriction.ITEM));
        Item item = repository.insert(universeName,
            itemBuilder.createItemBuilder(universeName).withType(type).withSubtype(subtype).buildItem());
        assertThat(item).isNotNull();

        assertThat(
            typeTranslationRepository.getAllVariants(universeName, subtype)).containsExactlyInAnyOrder(type,
            subtype);
    }

    @Override
    protected Item createObject() {
        return itemBuilder.createItemBuilder(universeName).withName("Test").buildItem();
    }

    @Override
    protected Item createSlightlyChangeObject() {
        return itemBuilder.createItemBuilder(universeName).withName("Test Differently").buildItem();
    }

    @Override
    protected Collection<Item> createMultipleObjects() {
        return List.of(itemBuilder.createItemBuilder(universeName).withName("A").buildItem(),
            itemBuilder.createItemBuilder(universeName).withName("B").buildItem(),
            itemBuilder.createItemBuilder(universeName).withName("C").buildItem());
    }
}
