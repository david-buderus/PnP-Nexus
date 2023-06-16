package de.pnp.manager.server.database;

import de.pnp.manager.component.item.Item;
import java.util.Optional;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class ItemRepository extends RepositoryBase<Item> {

  public static final String REPOSITORY_NAME = "items";

  public ItemRepository() {
    super(Item.class, REPOSITORY_NAME);
  }

  public Optional<Item> get(String universe, String name) {
    return Optional.ofNullable(
        getTemplate(universe).findOne(Query.query(Criteria.where("name").is(name)), Item.class,
            REPOSITORY_NAME));
  }
}
