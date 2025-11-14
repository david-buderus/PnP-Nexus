package de.pnp.manager.server.database.character;

import com.mongodb.DBRef;
import de.pnp.manager.component.character.Species;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Repository for {@link Species species}.
 */
@Component
public class SpeciesRepository extends RepositoryBase<Species> implements IUniquelyNamedRepository<Species> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "species";

    protected SpeciesRepository() {
        super(Species.class, REPOSITORY_NAME);
    }

    /**
     * Removes all references to that nation from all species.
     */
    public void removeNationReferences(ObjectId universe, List<ObjectId> nationIds) {
        if (nationIds == null || nationIds.isEmpty()) {
            return;
        }

        List<DBRef> nationRefs = nationIds.stream()
                .map(id -> new DBRef("nation", id))
                .toList();

        Query query = new Query(Criteria.where("nations").in(nationRefs));
        Update update = new Update().pullAll("nations", nationRefs.toArray());
        updateMulti(universe, query, update);
    }
}
