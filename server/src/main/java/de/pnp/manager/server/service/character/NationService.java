package de.pnp.manager.server.service.character;

import de.pnp.manager.component.character.Nation;
import de.pnp.manager.server.database.character.NationRepository;
import de.pnp.manager.server.service.RepositoryServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link NationRepository}.
 */
@RestController
@RequestMapping("api/{universe}/nations")
public class NationService extends RepositoryServiceBase<Nation, NationRepository> {

    protected NationService(@Autowired NationRepository repository) {
        super(repository);
    }
}
