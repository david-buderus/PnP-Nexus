package de.pnp.manager.server.database;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link Item items}
 */
@Component
public class ItemRepository extends RepositoryBase<Item> implements IUniquelyNamedRepository<Item> {

  /**
   * Name of the repository
   */
  public static final String REPOSITORY_NAME = "items";

  @Autowired
  private ItemTypeTranslationRepository typeTranslationRepository;

  public ItemRepository() {
    super(Item.class, REPOSITORY_NAME);
  }

  @Override
  protected void onAfterPersistent(String universe, Item item) {
    typeTranslationRepository.addTypeTranslation(universe, item.getSubtype(),
        item.getType());
  }
}
