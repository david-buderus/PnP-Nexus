package de.pnp.manager.server.database.character;

import de.pnp.manager.component.character.PnPCharacter;
import de.pnp.manager.server.database.RepositoryBase;
import org.springframework.stereotype.Component;

/**
 * Repository for {@link PnPCharacterRepository characters}.
 */
@Component
public class PnPCharacterRepository extends RepositoryBase<PnPCharacter> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "characters";

    protected PnPCharacterRepository() {
        super(PnPCharacter.class, REPOSITORY_NAME);
    }
}
