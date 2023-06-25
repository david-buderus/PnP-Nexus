package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.TypeTranslation;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link TypeTranslationRepository}.
 */
public class ItemTypeTranslationRepositoryTest extends
    RepositoryTestBase<TypeTranslation, TypeTranslationRepository> {

  public ItemTypeTranslationRepositoryTest(@Autowired TypeTranslationRepository repository) {
    super(repository);
  }

  @Test
  void testAddTypeTranslation() {
    String name = "One-handed Sword";
    TypeTranslation typeTranslation = repository.addTypeTranslation(universe, name,
        List.of("Weapon", "One-handed weapon"));

    assertThat(typeTranslation.getName()).isEqualTo(name);
    assertThat(typeTranslation.getBroaderVariants()).containsExactlyInAnyOrder("Weapon",
        "One-handed weapon");

    typeTranslation = repository.addTypeTranslation(universe, name, List.of("Blade"));

    assertThat(typeTranslation.getName()).isEqualTo(name);
    assertThat(typeTranslation.getBroaderVariants()).containsExactlyInAnyOrder("Weapon",
        "One-handed weapon", "Blade");
  }

  @Test
  void testTransitiveGet() {
    repository.addTypeTranslation(universe, "One-handed sword",
        List.of("Sword", "One-handed weapon"));
    repository.addTypeTranslation(universe, "Sword",
        List.of("Weapon", "Blade"));
    repository.addTypeTranslation(universe, "One-handed weapon",
        List.of("Weapon"));

    assertThat(repository.getAllVariants(universe, "One-handed sword")).containsExactlyInAnyOrder(
        "One-handed sword", "Sword", "One-handed weapon", "Weapon", "Blade");
  }

  @Test
  @Timeout(1)
  void testLoopSafety() {
    repository.addTypeTranslation(universe, "A", List.of("B"));
    repository.addTypeTranslation(universe, "B", List.of("A"));

    assertThat(repository.getAllVariants(universe, "A")).containsExactlyInAnyOrder("A", "B");
  }

  @Override
  protected TypeTranslation createObject() {
    return new TypeTranslation(null, "Sword", List.of("Weapon"));
  }

  @Override
  protected TypeTranslation createSlightlyChangeObject() {
    return new TypeTranslation(null, "Sword", List.of("Blade", "Weapon"));
  }
}
