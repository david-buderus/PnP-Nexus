package de.pnp.manager.server.database;

import de.pnp.manager.component.character.Talent;
import java.util.Collection;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link Talent talents}.
 */
@Component
public class TalentRepository extends RepositoryBase<Talent> {

  /**
   * Name of the repository
   */
  public static final String REPOSITORY_NAME = "talent";

  public TalentRepository() {
    super(Talent.class, REPOSITORY_NAME);
  }

  /**
   * Returns all {@link Talent talents} with the given name.
   */
  public Collection<Talent> getByName(String universe, String name) {
    return getAll(universe, Query.query(Criteria.where("name").is(name)));
  }
}
