package de.pnp.manager.server.database.item;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.RepositoryTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        Material material = materialRepository.insert(getUniverseName(),
                new Material(null, "ArmorMat", Collections.emptyList()));

        Armor armor = itemBuilder.createItemBuilder(getUniverseName()).withMaterial(material).buildArmor();
        Item persistedArmor = repository.insert(getUniverseName(), armor);

        assertThat(repository.getAll(getUniverseName())).contains(armor);
        assertThat(repository.get(getUniverseName(), armor.getName())).contains(armor);
        assertThat(repository.get(getUniverseName(), persistedArmor.getId())).contains(armor);
    }

    @Test
    void testTypeLink() {
        ItemType typeA = typeRepository.insert(getUniverseName(),
                new ItemType(null, "Type A", ETypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", ETypeRestriction.ITEM);
        Item item = itemBuilder.createItemBuilder(getUniverseName()).withType(typeA).buildItem();

        testRepositoryLink(Item::getType, typeRepository, item, typeA, typeB);
    }

    @Test
    void testSubtypeLink() {
        ItemType typeA = typeRepository.insert(getUniverseName(),
                new ItemType(null, "Type A", ETypeRestriction.ITEM));
        ItemType typeB = new ItemType(null, "Type B", ETypeRestriction.ITEM);
        Item item = itemBuilder.createItemBuilder(getUniverseName()).withSubtype(typeA).buildItem();

        testRepositoryLink(Item::getSubtype, typeRepository, item, typeA, typeB);
    }

    @Test
    void testMaterialLink() {
        Material materialA = materialRepository.insert(getUniverseName(),
                new Material(null, "Material A", Collections.emptyList()));
        Material materialB = new Material(null, "Material B", Collections.emptyList());
        Weapon weapon = itemBuilder.createItemBuilder(getUniverseName()).withName("Test").withMaterial(materialA)
                .buildWeapon();

        testRepositoryLink(Weapon::getMaterial, materialRepository, weapon, materialA, materialB);
    }


    @Test
    void testAutomaticTypeTranslations() {
        ItemType type = typeRepository.insert(getUniverseName(),
                new ItemType(null, "Type", ETypeRestriction.ITEM));
        ItemType subtype = typeRepository.insert(getUniverseName(),
                new ItemType(null, "Subtype", ETypeRestriction.ITEM));
        Item item = repository.insert(getUniverseName(),
                itemBuilder.createItemBuilder(getUniverseName()).withType(type).withSubtype(subtype).buildItem());
        assertThat(item).isNotNull();

        assertThat(
                typeTranslationRepository.getAllVariants(getUniverseName(), subtype)).containsExactlyInAnyOrder(type,
                subtype);
    }

    @Override
    protected Item createObject() {
        return itemBuilder.createItemBuilder(getUniverseName()).withName("Test").buildItem();
    }

    @Override
    protected Item createSlightlyChangeObject() {
        return itemBuilder.createItemBuilder(getUniverseName()).withName("Test Differently").buildItem();
    }

    @Override
    protected List<Item> createMultipleObjects() {
        return List.of(itemBuilder.createItemBuilder(getUniverseName()).withName("A").buildItem(),
                itemBuilder.createItemBuilder(getUniverseName()).withName("B").buildItem(),
                itemBuilder.createItemBuilder(getUniverseName()).withName("C").buildItem());
    }
}
