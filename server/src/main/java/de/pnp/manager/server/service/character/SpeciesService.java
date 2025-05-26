package de.pnp.manager.server.service.character;

import de.pnp.manager.component.character.Species;
import de.pnp.manager.server.database.character.SpeciesRepository;
import de.pnp.manager.server.service.RepositoryServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link SpeciesRepository}.
 */
@RestController
@RequestMapping("api/{universe}/species")
public class SpeciesService extends RepositoryServiceBase<Species, SpeciesRepository> {
    
    protected SpeciesService(@Autowired SpeciesRepository repository) {
        super(repository);
    }
}
