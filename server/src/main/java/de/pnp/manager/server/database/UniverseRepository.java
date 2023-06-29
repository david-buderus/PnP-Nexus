package de.pnp.manager.server.database;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.result.DeleteResult;
import de.pnp.manager.component.Universe;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class UniverseRepository {

  public static final String DATABASE_NAME = "metadata";

  public static final String REPOSITORY_NAME = "universe";

  private static final Set<String> FORBIDDEN_UNIVERSE_NAMES = Set.of(DATABASE_NAME, "admin",
      "local", "config");

  private final MongoTemplate mongoTemplate;
  private final MongoClient mongoClient;

  public UniverseRepository(@Autowired MongoConfig config) {
    mongoTemplate = config.mongoTemplate(DATABASE_NAME);
    mongoClient = config.mongo();
  }

  public Optional<Universe> get(String name) {
    return Optional.ofNullable(
        mongoTemplate.findOne(Query.query(Criteria.where("name").is(name)), Universe.class,
            REPOSITORY_NAME));
  }

  /**
   * Returns all universes.
   */
  public Collection<Universe> getAll() {
    return mongoTemplate.findAll(Universe.class, REPOSITORY_NAME);
  }

  public boolean exists(String name) {
    return mongoTemplate.exists(Query.query(Criteria.where("name").is(name)), Universe.class);
  }

  public Universe insert(Universe universe) {
    Preconditions.checkArgument(!FORBIDDEN_UNIVERSE_NAMES.contains(universe.getName()));
    Preconditions.checkArgument(!exists(universe.getName()));
    return mongoTemplate.insert(universe, REPOSITORY_NAME);
  }

  public Universe update(Universe universe) {
    return mongoTemplate.findAndReplace(
        Query.query(Criteria.where("name").is(universe.getName())),
        universe, REPOSITORY_NAME);
  }

  public boolean remove(String universe) {
    Preconditions.checkNotNull(universe);
    DeleteResult result = mongoTemplate.remove(Query.query(Criteria.where("_id").is(universe)),
        REPOSITORY_NAME);
    if (!result.wasAcknowledged() && result.getDeletedCount() == 1) {
      return false;
    }
    mongoClient.getDatabase(universe).drop();
    return true;
  }
}
