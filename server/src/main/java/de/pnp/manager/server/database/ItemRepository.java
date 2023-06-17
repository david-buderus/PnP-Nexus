package de.pnp.manager.server.database;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.database.interfaces.IUniqueNameRepository;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link Item items}
 */
@Component
public class ItemRepository extends RepositoryBase<Item> implements IUniqueNameRepository<Item> {

  /**
   * Name of the repository
   */
  public static final String REPOSITORY_NAME = "items";

  public ItemRepository() {
    super(Item.class, REPOSITORY_NAME);
  }
}
