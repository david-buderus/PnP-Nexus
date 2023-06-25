package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.TypeRestriction;
import de.pnp.manager.component.item.ItemTypeTranslation;
import java.util.List;
import java.util.Map;
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
    ItemTypeTranslation typeTranslation = repository.addTypeTranslation(universe, type,
        List.of(asType("Weapon"), asType("One-handed weapon")));

    assertThat(typeTranslation.getType()).isEqualTo(type);
    assertThat(typeTranslation.getBroaderVariants()).containsExactlyInAnyOrder(asType("Weapon"),
        asType("One-handed weapon"));

    typeTranslation = repository.addTypeTranslation(universe, type, List.of(asType("Blade")));

    assertThat(typeTranslation.getType()).isEqualTo(type);
    assertThat(typeTranslation.getBroaderVariants()).containsExactlyInAnyOrder(asType("Weapon"),
        asType("One-handed weapon"), asType("Blade"));
  }

  @Test
  void testTransitiveGet() {
    repository.addTypeTranslation(universe, asType("One-handed sword"),
        List.of(asType("Sword"), asType("One-handed weapon")));
    repository.addTypeTranslation(universe, asType("Sword"),
        List.of(asType("Weapon"), asType("Blade")));
    repository.addTypeTranslation(universe, asType("One-handed weapon"),
        List.of(asType("Weapon")));

    assertThat(
        repository.getAllVariants(universe, asType("One-handed sword"))).containsExactlyInAnyOrder(
        asType("One-handed sword"), asType("Sword"), asType("One-handed weapon"), asType("Weapon"),
        asType("Blade"));
  }

  @Test
  @Timeout(1)
  void testLoopSafety() {
    repository.addTypeTranslation(universe, asType("A"), List.of(asType("B")));
    repository.addTypeTranslation(universe, asType("B"), List.of(asType("A")));

    assertThat(repository.getAllVariants(universe, asType("A"))).containsExactlyInAnyOrder(
        asType("A"), asType("B"));
  }

  @Test
  void testTypeLink() {
    ItemType typeA = typeRepository.insert(universe,
        new ItemType(null, "Type A", TypeRestriction.ITEM));
    ItemType typeB = new ItemType(null, "Type B", TypeRestriction.WEAPON);
    ItemTypeTranslation typeTranslation = new ItemTypeTranslation(null, typeA, List.of());

    testRepositoryLink(ItemTypeTranslation::getType, typeRepository, typeTranslation, typeA, typeB);
  }

  @Test
  void testVariantsLink() {
    ItemType typeA = typeRepository.insert(universe,
        new ItemType(null, "Type A", TypeRestriction.ITEM));
    ItemType typeB = new ItemType(null, "Type B", TypeRestriction.WEAPON);
    ItemTypeTranslation typeTranslation = new ItemTypeTranslation(null, asType("test"),
        List.of(typeA));

    testRepositoryCollectionLink(ItemTypeTranslation::getBroaderVariants, typeRepository,
        typeTranslation, List.of(typeA), Map.of(typeA, typeB));
  }

  @Override
  protected ItemTypeTranslation createObject() {
    return new ItemTypeTranslation(null, asType("Sword"), List.of(asType("Weapon")));
  }

  @Override
  protected ItemTypeTranslation createSlightlyChangeObject() {
    return new ItemTypeTranslation(null, asType("Sword"),
        List.of(asType("Blade"), asType("Weapon")));
  }

  /**
   * Returns an already persisted {@link ItemType} with the given name.
   */
  protected ItemType asType(String type) {
    return typeRepository.get(universe, type)
        .orElse(typeRepository.insert(universe, new ItemType(null, type, TypeRestriction.ITEM)));
  }
}
