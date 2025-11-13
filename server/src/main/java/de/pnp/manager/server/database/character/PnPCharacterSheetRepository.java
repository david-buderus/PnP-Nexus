package de.pnp.manager.server.database.character;

import de.pnp.manager.component.character.PnPCharacterSheet;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link PnPCharacterSheet character sheets}.
 */
@Component
public class PnPCharacterSheetRepository extends RepositoryBase<PnPCharacterSheet> implements IUniquelyNamedRepository<PnPCharacterSheet> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "character-sheets";

    protected PnPCharacterSheetRepository() {
        super(PnPCharacterSheet.class, REPOSITORY_NAME);
    }
}
