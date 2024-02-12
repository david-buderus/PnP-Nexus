package de.pnp.manager.server.database;

import static de.pnp.manager.server.database.UniverseRepository.DATABASE_NAME;

import com.mongodb.client.result.DeleteResult;
import de.pnp.manager.component.user.PnPUser;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.server.contoller.UserController;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link PnPUser users}.
 */
@Component
public class UserRepository {

    /**
     * Name of the user repository.
     */
    public static final String REPOSITORY_NAME = "user";

    private final MongoTemplate mongoTemplate;

    public UserRepository(@Autowired MongoConfig config) {
        mongoTemplate = config.mongoTemplate(DATABASE_NAME);
    }

    /**
     * Returns all users.
     */
    public Collection<PnPUser> getAllUsers() {
        return mongoTemplate.findAll(PnPUser.class, REPOSITORY_NAME);
    }

    /**
     * Returns all users for the given usernames.
     */
    public Collection<PnPUser> getAllUsers(Collection<String> usernames) {
        return mongoTemplate.find(Query.query(Criteria.where("_id").in(usernames)), PnPUser.class, REPOSITORY_NAME);
    }

    /**
     * Returns the {@link PnPUser} of the given user.
     */
    public Optional<PnPUser> getUser(String username) {
        return Optional.ofNullable(
            mongoTemplate.findById(username, PnPUser.class, REPOSITORY_NAME));
    }

    /**
     * Returns the {@link PnPUser} of the given user.
     */
    public Optional<PnPUser> getUserByDisplayName(String displayName) {
        return Optional.ofNullable(
            mongoTemplate.findOne(Query.query(Criteria.where("displayName").is(displayName)), PnPUser.class,
                REPOSITORY_NAME));
    }

    /**
     * Inserts the user into the database.
     * <p>
     * Don't call this directly. Use {@link UserController#createNewUser(PnPUserCreation)}.
     */
    public void addNewUser(PnPUser user) {
        mongoTemplate.insert(user, REPOSITORY_NAME);
    }

    /**
     * Updates the user in the database.
     */
    public void updateUser(PnPUser user) {
        mongoTemplate.findAndReplace(Query.query(Criteria.where("_id").is(user.getUsername())), user, REPOSITORY_NAME);
    }

    /**
     * Removes the user from the database.
     * <p>
     * Don't call this directly. Use {@link UserController#removeUser(String)}.
     */
    public boolean removeUser(String username) {
        DeleteResult result = mongoTemplate.remove(Query.query(Criteria.where("_id").is(username)),
            REPOSITORY_NAME);
        return result.wasAcknowledged();
    }
}
