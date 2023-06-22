package de.pnp.manager.server.database;

import de.pnp.manager.component.item.TypeTranslation;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * The repository of {@link TypeTranslation}.
 */
@Component
public class TypeTranslationRepository extends RepositoryBase<TypeTranslation> implements
    IUniquelyNamedRepository<TypeTranslation> {

  /**
   * Name of the repository
   */
  public static final String REPOSITORY_NAME = "type-translations";

  public TypeTranslationRepository() {
    super(TypeTranslation.class, REPOSITORY_NAME);
  }

  /**
   * Adds the given variants as a broader type to the given name.
   */
  public TypeTranslation addTypeTranslation(String universe, String name,
      Collection<String> broaderVariants) {
    Optional<TypeTranslation> optTranslation = get(universe, name);

    if (optTranslation.isEmpty()) {
      return insert(universe, new TypeTranslation(null, name, broaderVariants));
    }

    TypeTranslation typeTranslation = optTranslation.get();
    ArrayList<String> variants = new ArrayList<>(broaderVariants);
    variants.addAll(typeTranslation.getBroaderVariants());

    return update(universe, typeTranslation.getId(),
        new TypeTranslation(typeTranslation.getId(), typeTranslation.getName(),
            variants));
  }

  /**
   * Returns all broader variants of the given name.
   */
  public Collection<String> getAllVariants(String universe, String name) {
    Set<String> result = new HashSet<>();
    result.add(name);

    Queue<String> queue = new LinkedList<>();
    Set<String> alreadySeen = new HashSet<>();
    alreadySeen.add(name);

    get(universe, name).ifPresent(
        typeTranslation -> queue.addAll(typeTranslation.getBroaderVariants()));

    while (!queue.isEmpty()) {
      String type = queue.poll();

      if (alreadySeen.contains(type)) {
        continue;
      }
      result.add(type);
      alreadySeen.add(type);

      get(universe, type).ifPresent(
          typeTranslation -> queue.addAll(typeTranslation.getBroaderVariants()));
    }

    return result;
  }
}
