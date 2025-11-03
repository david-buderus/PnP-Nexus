package de.pnp.manager.server.database;

import de.pnp.manager.Tag;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    protected void onAfterPersist(String universe, List<Spell> objects) {
        super.onAfterPersist(universe, objects);

        Set<Tag> newTags = new HashSet<>();
        for (Spell spell : objects) {
            newTags.addAll(spell.getTags());
        }
        tagRepository.saveAll(universe, newTags);
    }
}
