package de.pnp.manager.server.database.character;

import de.pnp.manager.component.character.Nation;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Repository for {@link Nation nation}.
 */
@Component
public class NationRepository extends RepositoryBase<Nation> implements IUniquelyNamedRepository<Nation> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "nation";

    @Autowired
    private SpeciesRepository speciesRepository;

    protected NationRepository() {
        super(Nation.class, REPOSITORY_NAME);
    }

    @Override
    protected void onAfterDeletion(ObjectId universe, List<ObjectId> ids) {
        super.onAfterDeletion(universe, ids);
        speciesRepository.removeNationReferences(universe, ids);
    }
}
