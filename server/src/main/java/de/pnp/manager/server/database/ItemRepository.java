package de.pnp.manager.server.database;

import de.pnp.manager.component.item.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemRepository extends RepositoryBase<Item> {

  public ItemRepository() {
    super(Item.class, "items");
  }
}
