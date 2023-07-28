package de.pnp.manager.server.database;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.result.DeleteResult;
import de.pnp.manager.component.Universe;
import de.pnp.manager.component.Universe.UniverseSettings;
import de.pnp.manager.server.exception.UniverseNotFoundException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link Universe universes}.
 */
@Component
public class UniverseRepository {

    /**
     * Name of the database which stores the {@link Universe universes}.
     */
    public static final String DATABASE_NAME = "metadata";

    /**
     * Name of the repository.
     */
    public static final String REPOSITORY_NAME = "universe";

    private static final Set<String> FORBIDDEN_UNIVERSE_NAMES = Set.of(DATABASE_NAME, "admin",
        "local", "config");

    private final MongoTemplate mongoTemplate;
    private final MongoClient mongoClient;

    public UniverseRepository(@Autowired MongoConfig config) {
        mongoTemplate = config.mongoTemplate(DATABASE_NAME);
        mongoClient = config.mongo();
    }

    /**
     * Returns the universe with the given {@link Universe#getName() name}.
     */
    public Optional<Universe> get(String name) {
        return Optional.ofNullable(
            mongoTemplate.findOne(Query.query(Criteria.where("name").is(name)), Universe.class,
                REPOSITORY_NAME));
    }

    /**
     * Returns all {@link Universe universes}.
     */
    public Collection<Universe> getAll() {
        return mongoTemplate.findAll(Universe.class, REPOSITORY_NAME);
    }

    /**
     * Returns whether a {@link Universe} with the given {@link Universe#getName() name} exists.
     */
    public boolean exists(String name) {
        return mongoTemplate.exists(Query.query(Criteria.where("name").is(name)), Universe.class);
    }

    /**
     * Inserts a new {@link Universe} in the database.
     */
    public Universe insert(Universe universe) {
        Preconditions.checkArgument(!FORBIDDEN_UNIVERSE_NAMES.contains(universe.getName()));
        Preconditions.checkArgument(!exists(universe.getName()));
        return mongoTemplate.insert(universe, REPOSITORY_NAME);
    }

    /**
     * Updates a {@link Universe}.
     */
    public Universe update(Universe universe) {
        return mongoTemplate.findAndReplace(
            Query.query(Criteria.where("name").is(universe.getName())),
            universe, REPOSITORY_NAME);
    }

    /**
     * Removes a {@link Universe} from the database.
     */
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

    /**
     * Returns the {@link UniverseSettings} of the given universe.
     */
    public UniverseSettings getSetting(String universe) {
        Optional<Universe> optUniverse = get(universe);
        if (optUniverse.isEmpty()) {
            throw new UniverseNotFoundException(universe);
        }
        return optUniverse.get().getSettings();
    }
}
