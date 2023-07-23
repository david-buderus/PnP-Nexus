package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.TypeRestriction;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Weapon;
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
        Armor armor = itemBuilder.createItemBuilder(universe).buildArmor();
        Item persistedArmor = repository.insert(universe, armor);

        assertThat(repository.getAll(universe)).contains(armor);
        assertThat(repository.get(universe, armor.getName())).contains(armor);
        assertThat(repository.get(universe, persistedArmor.getId())).contains(armor);
    }

    @Test
    void testTypeLink() {
        ItemType typeA = typeRepository.insert(universe,
            new ItemType(null, "Type A", TypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", TypeRestriction.ITEM);
        Item item = itemBuilder.createItemBuilder(universe).withType(typeA).buildItem();

        testRepositoryLink(Item::getType, typeRepository, item, typeA, typeB);
    }

    @Test
    void testSubtypeLink() {
        ItemType typeA = typeRepository.insert(universe,
            new ItemType(null, "Type A", TypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", TypeRestriction.ITEM);
        Item item = itemBuilder.createItemBuilder(universe).withSubtype(typeA).buildItem();

        testRepositoryLink(Item::getSubtype, typeRepository, item, typeA, typeB);
    }

    @Test
    void testMaterialLink() {
        Material materialA = materialRepository.insert(universe,
            new Material(null, "Material A", Collections.emptyList()));
        Material materialB = new Material(null, "Material B", Collections.emptyList());
        Weapon weapon = itemBuilder.createItemBuilder(universe).withName("Test").withMaterial(materialA)
            .buildWeapon();

        testRepositoryLink(Weapon::getMaterial, materialRepository, weapon, materialA, materialB);
    }


    @Test
    void testAutomaticTypeTranslations() {
        ItemType type = typeRepository.insert(universe,
            new ItemType(null, "Type", TypeRestriction.ITEM));
        ItemType subtype = typeRepository.insert(universe,
            new ItemType(null, "Subtype", TypeRestriction.ITEM));
        Item item = repository.insert(universe,
            itemBuilder.createItemBuilder(universe).withType(type).withSubtype(subtype).buildItem());
        assertThat(item).isNotNull();

        assertThat(
            typeTranslationRepository.getAllVariants(universe, subtype)).containsExactlyInAnyOrder(type,
            subtype);
    }

    @Override
    protected Item createObject() {
        return itemBuilder.createItemBuilder(universe).withName("Test").buildItem();
    }

    @Override
    protected Item createSlightlyChangeObject() {
        return itemBuilder.createItemBuilder(universe).withName("Test Differently").buildItem();
    }

    @Override
    protected List<Item> createMultipleObjects() {
        return List.of(itemBuilder.createItemBuilder(universe).withName("A").buildItem(),
            itemBuilder.createItemBuilder(universe).withName("B").buildItem(),
            itemBuilder.createItemBuilder(universe).withName("C").buildItem());
    }
}
