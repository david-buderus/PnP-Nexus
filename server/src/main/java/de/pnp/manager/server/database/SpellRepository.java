package de.pnp.manager.server.database;

import de.pnp.manager.Tag;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Repository for {@link Spell spells}.
 */
@Component
public class SpellRepository extends RepositoryBase<Spell> implements IUniquelyNamedRepository<Spell> {

    @Autowired
    private TagRepository tagRepository;

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "spells";

    public SpellRepository() {
        super(Spell.class, REPOSITORY_NAME);
    }

    @Override
    protected void onAfterPersist(ObjectId universe, List<Spell> objects) {
        super.onAfterPersist(universe, objects);

        Set<Tag> newTags = new HashSet<>();
        for (Spell spell : objects) {
            newTags.addAll(spell.getTags());
        }
        tagRepository.saveAll(universe, newTags);
    }
}
