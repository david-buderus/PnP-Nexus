package de.pnp.manager.server.database;

import de.pnp.manager.component.Spell;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link Spell spells}.
 */
@Component
public class SpellRepository extends RepositoryBase<Spell> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "spells";

    public SpellRepository() {
        super(Spell.class, REPOSITORY_NAME);
    }
}
