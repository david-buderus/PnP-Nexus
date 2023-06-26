package de.pnp.manager.server.database;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemTypeTranslation;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * The repository of {@link ItemTypeTranslation}.
 */
@Component
public class ItemTypeTranslationRepository extends RepositoryBase<ItemTypeTranslation> {

  /**
   * Name of the repository
   */
  public static final String REPOSITORY_NAME = "type-translations";

  public ItemTypeTranslationRepository() {
    super(ItemTypeTranslation.class, REPOSITORY_NAME);
  }

  /**
   * Returns the {@link ItemTypeTranslation} which translates the given {@link ItemType}.
   */
  public Optional<ItemTypeTranslation> get(String universe, ItemType type) {
    return get(universe, Query.query(Criteria.where("type").is(type)));
  }

  /**
   * Adds the given variant as a broader type to the given type.
   */
  public ItemTypeTranslation addTypeTranslation(String universe, ItemType type,
      ItemType broaderVariant) {
    return addTypeTranslation(universe, type, Set.of(broaderVariant));
  }

  /**
   * Adds the given variants as a broader type to the given type.
   */
  public ItemTypeTranslation addTypeTranslation(String universe, ItemType type,
      Set<ItemType> broaderVariants) {
    Optional<ItemTypeTranslation> optTranslation = get(universe, type);

    if (optTranslation.isEmpty()) {
      return insert(universe, new ItemTypeTranslation(null, type, broaderVariants));
    }

    ItemTypeTranslation typeTranslation = optTranslation.get();
    Set<ItemType> variants = new HashSet<>(broaderVariants);
    variants.addAll(typeTranslation.getBroaderVariants());

    if (variants.size() == typeTranslation.getBroaderVariants().size()) {
      // No new types were added
      return typeTranslation;
    }

    return update(universe, typeTranslation.getId(),
        new ItemTypeTranslation(typeTranslation.getId(), typeTranslation.getType(),
            variants));
  }

  /**
   * Returns all broader variants of the given type.
   */
  public Collection<ItemType> getAllVariants(String universe, ItemType type) {
    Set<ItemType> result = new HashSet<>();
    result.add(type);

    Queue<ItemType> queue = new LinkedList<>();
    Set<ItemType> alreadySeen = new HashSet<>();
    alreadySeen.add(type);

    get(universe, type).ifPresent(
        typeTranslation -> queue.addAll(typeTranslation.getBroaderVariants()));

    while (!queue.isEmpty()) {
      ItemType currentType = queue.poll();

      if (alreadySeen.contains(currentType)) {
        continue;
      }
      result.add(currentType);
      alreadySeen.add(currentType);

      get(universe, currentType).ifPresent(
          typeTranslation -> queue.addAll(typeTranslation.getBroaderVariants()));
    }

    return result;
  }
}
