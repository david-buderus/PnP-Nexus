package de.pnp.manager.server.service;

import de.pnp.manager.component.Spell;
import de.pnp.manager.server.database.SpellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link SpellRepository}.
 */
@RestController
@RequestMapping("/api/{universe}/spells")
public class SpellService extends RepositoryServiceBase<Spell, SpellRepository> {

    protected SpellService(@Autowired SpellRepository repository) {
        super(repository);
    }
}
