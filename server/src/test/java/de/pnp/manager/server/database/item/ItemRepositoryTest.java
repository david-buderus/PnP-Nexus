package de.pnp.manager.server.database.item;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.RepositoryTestBase;
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
        Material material = materialRepository.insert(universeName,
            new Material(null, "ArmorMat", Collections.emptyList()));

        Armor armor = createItem().withMaterial(material).buildArmor();
        Item persistedArmor = repository.insert(universeName, armor);

        assertThat(repository.getAll(universeName)).contains(armor);
        assertThat(repository.get(universeName, armor.getName())).contains(armor);
        assertThat(repository.get(universeName, persistedArmor.getId())).contains(armor);
    }

    @Test
    void testTypeLink() {
        ItemType typeA = typeRepository.insert(universeName,
            new ItemType(null, "Type A", ETypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", ETypeRestriction.ITEM);
        Item item = createItem().withType(typeA).buildItem();

        testRepositoryLink(Item::getType, typeRepository, item, typeA, typeB);
    }

    @Test
    void testSubtypeLink() {
        ItemType typeA = typeRepository.insert(universeName,
            new ItemType(null, "Type A", ETypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", ETypeRestriction.ITEM);
        Item item = createItem().withSubtype(typeA).buildItem();

        testRepositoryLink(Item::getSubtype, typeRepository, item, typeA, typeB);
    }

    @Test
    void testMaterialLink() {
        Material materialA = materialRepository.insert(universeName,
            new Material(null, "Material A", Collections.emptyList()));
        Material materialB = new Material(null, "Material B", Collections.emptyList());
        Weapon weapon = createItem().withName("Test").withMaterial(materialA)
            .buildWeapon();

        testRepositoryLink(Weapon::getMaterial, materialRepository, weapon, materialA, materialB);
    }


    @Test
    void testAutomaticTypeTranslations() {
        ItemType type = typeRepository.insert(universeName,
            new ItemType(null, "Type", ETypeRestriction.ITEM));
        ItemType subtype = typeRepository.insert(universeName,
            new ItemType(null, "Subtype", ETypeRestriction.ITEM));
        Item item = repository.insert(universeName,
            createItem().withType(type).withSubtype(subtype).buildItem());
        assertThat(item).isNotNull();

        assertThat(
            typeTranslationRepository.getAllVariants(universeName, subtype)).containsExactlyInAnyOrder(type,
            subtype);
    }

    @Override
    protected Item createObject() {
        return createItem().withName("Test").buildItem();
    }

    @Override
    protected Item createSlightlyChangeObject() {
        return createItem().withName("Test Differently").buildItem();
    }

    @Override
    protected List<Item> createMultipleObjects() {
        return List.of(createItem().withName("A").buildItem(),
            createItem().withName("B").buildItem(),
            createItem().withName("C").buildItem());
    }
}
