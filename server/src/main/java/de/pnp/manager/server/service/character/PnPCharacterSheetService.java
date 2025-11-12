package de.pnp.manager.server.service.character;

import de.pnp.manager.component.character.PnPCharacterSheet;
import de.pnp.manager.server.database.character.PnPCharacterSheetRepository;
import de.pnp.manager.server.service.RepositoryServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link PnPCharacterSheetRepository}.
 */
@RestController
@RequestMapping("api/{universe}/character-sheets")
public class PnPCharacterSheetService extends RepositoryServiceBase<PnPCharacterSheet, PnPCharacterSheetRepository> {

    public PnPCharacterSheetService(@Autowired PnPCharacterSheetRepository repository) {
        super(repository);
    }
}
