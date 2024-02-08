package de.pnp.manager.server.service;

import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.database.TalentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link TalentRepository}.
 */
@RestController
@RequestMapping("/api/{universe}/talents")
public class TalentService extends RepositoryServiceBase<Talent, TalentRepository> {

    protected TalentService(@Autowired TalentRepository repository) {
        super(repository);
    }
}
