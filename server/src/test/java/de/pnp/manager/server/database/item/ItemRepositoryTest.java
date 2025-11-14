package de.pnp.manager.server.database.item;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
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
    private MaterialRepository materialRepository;

    public ItemRepositoryTest(@Autowired ItemRepository repository) {
        super(repository);
    }

    @Test
    void testInsertArmor() {
        Material material = materialRepository.insert(getUniverseId(),
                new Material(null, "ArmorMat", Collections.emptyList()));

        Armor armor = createItem().withMaterial(material).buildArmor();
        Item persistedArmor = repository.insert(getUniverseId(), armor);

        assertThat(repository.getAll(getUniverseId())).contains(armor);
        assertThat(repository.get(getUniverseId(), armor.getName())).contains(armor);
        assertThat(repository.get(getUniverseId(), persistedArmor.getId())).contains(armor);
    }

    @Test
    void testMixedInput() {
        Material material = materialRepository.insert(getUniverseId(),
                new Material(null, "Mat", Collections.emptyList()));

        Armor armor1 = createItem().withName("A1").withMaterial(material).persist().buildArmor();
        Armor armor2 = createItem().withName("A2").withMaterial(material).persist().buildArmor();
        Weapon weapon = createItem().withName("W1").withMaterial(material).persist().buildWeapon();
        Jewellery jewellery = createItem().withName("J1").withMaterial(material).persist().buildJewellery();
        Shield shield = createItem().withName("S1").withMaterial(material).persist().buildShield();
        createItem().withName("E1").persist().buildItem();

        assertThat(repository.getAllArmor(getUniverseId())).containsExactlyInAnyOrder(armor1, armor2);
        assertThat(repository.getAllWeapons(getUniverseId())).containsExactlyInAnyOrder(weapon);
        assertThat(repository.getAllJewellery(getUniverseId())).containsExactlyInAnyOrder(jewellery);
        assertThat(repository.getAllShields(getUniverseId())).containsExactlyInAnyOrder(shield);
    }

    @Test
    void testMaterialLink() {
        Material materialA = materialRepository.insert(getUniverseId(),
                new Material(null, "Material A", Collections.emptyList()));
        Material materialB = new Material(null, "Material B", Collections.emptyList());
        Weapon weapon = createItem().withName("Test").withMaterial(materialA)
                .buildWeapon();

        testRepositoryLink(Weapon::getMaterial, materialRepository, weapon, materialA, materialB);
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
