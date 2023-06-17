package de.pnp.manager.server.database.interfaces;

import java.util.Optional;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Mixin to provide methods to get an object by a unique name.
 */
public interface IUniqueNameRepository<E> {

  /**
   * Returns the object with the given name.
   */
  default Optional<E> get(String universe, String name) {
    return get(universe, Query.query(Criteria.where("name").is(name)));
  }

  /**
   * Returns the object which matches the {@link Query}.
   */
  Optional<E> get(String universe, Query query);
}
