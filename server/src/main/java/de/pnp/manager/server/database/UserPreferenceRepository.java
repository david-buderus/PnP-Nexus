package de.pnp.manager.server.database;

import static de.pnp.manager.server.database.DatabaseConstants.METADATA_DATABASE;

import com.mongodb.client.result.DeleteResult;
import de.pnp.manager.component.user.PnPUserCreation;
import de.pnp.manager.component.user.PnPUserPreference;
import de.pnp.manager.server.contoller.UserController;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link PnPUserPreference preferences}.
 */
@Component
public class UserPreferenceRepository {

    /**
     * Name of the user repository.
     */
    public static final String REPOSITORY_NAME = "user-preference";

    private final MongoTemplate mongoTemplate;

    public UserPreferenceRepository(@Autowired MongoConfig config) {
        mongoTemplate = config.mongoTemplate(METADATA_DATABASE);
    }

    /**
     * Returns the {@link PnPUserPreference} of the given user.
     */
    public Optional<PnPUserPreference> getPreference(String username) {
        return Optional.ofNullable(
            mongoTemplate.findById(username, PnPUserPreference.class, REPOSITORY_NAME));
    }

    /**
     * Inserts the preference into the database.
     * <p>
     * Don't call this directly. Use {@link UserController#createNewUser(PnPUserCreation)}.
     */
    public void addNewPreference(PnPUserPreference preference) {
        mongoTemplate.insert(preference, REPOSITORY_NAME);
    }

    /**
     * Updates the preference in the database.
     */
    public void updateUser(PnPUserPreference preference) {
        mongoTemplate.findAndReplace(Query.query(Criteria.where("_id").is(preference.username())), preference,
            REPOSITORY_NAME);
    }

    /**
     * Removes the preference from the database.
     * <p>
     * Don't call this directly. Use {@link UserController#removeUser(String)}.
     */
    public boolean removeUser(String username) {
        DeleteResult result = mongoTemplate.remove(Query.query(Criteria.where("_id").is(username)),
            REPOSITORY_NAME);
        return result.wasAcknowledged();
    }
}
