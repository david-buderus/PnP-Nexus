package de.pnp.manager.server.database.universe;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.result.DeleteResult;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.database.DatabaseConstants;
import de.pnp.manager.server.database.MongoConfig;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

/**
 * Repository for {@link Universe universes}.
 */
@Component
public class UniverseRepository {

    /**
     * Name of the repository.
     */
    public static final String REPOSITORY_NAME = "universe";

    private final MongoTemplate mongoTemplate;
    private final MongoClient mongoClient;

    public UniverseRepository(@Autowired MongoConfig config) {
        mongoTemplate = config.mongoTemplate(DatabaseConstants.METADATA_DATABASE);
        mongoClient = config.mongo();
    }

    /**
     * Returns the universe with the given {@link Universe#getId() ID}.
     */
    public Optional<Universe> get(ObjectId id) {
        return Optional.ofNullable(
                mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), Universe.class,
                        REPOSITORY_NAME));
    }

    /**
     * Returns all {@link Universe universes}.
     */
    public Collection<Universe> getAll() {
        return mongoTemplate.findAll(Universe.class, REPOSITORY_NAME);
    }

    /**
     * Returns whether a {@link Universe} with the given {@link Universe#getId() name} exists.
     */
    public boolean exists(ObjectId id) {
        return mongoTemplate.exists(Query.query(Criteria.where("_id").is(id)), Universe.class);
    }

    /**
     * Inserts a new {@link Universe} in the database.
     */
    public Universe insert(Universe universe) {
        return mongoTemplate.insert(universe, REPOSITORY_NAME);
    }

    /**
     * Updates a {@link Universe}.
     */
    public Universe update(Universe universe) {
        return mongoTemplate.findAndReplace(
                Query.query(Criteria.where("_id").is(universe.getId())),
                universe, REPOSITORY_NAME);
    }

    /**
     * Removes a {@link Universe} from the database.
     */
    public boolean remove(ObjectId id) {
        Preconditions.checkNotNull(id);
        DeleteResult result = mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)),
                REPOSITORY_NAME);
        if (!result.wasAcknowledged() && result.getDeletedCount() == 1) {
            return false;
        }
        mongoClient.getDatabase(DatabaseConstants.UNIVERSE_PREFIX + id).drop();
        return true;
    }
}
