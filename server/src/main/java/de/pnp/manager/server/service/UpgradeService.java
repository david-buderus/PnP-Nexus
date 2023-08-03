package de.pnp.manager.server.service;

import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.server.database.upgrade.UpgradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link UpgradeRepository}.
 */
@RestController
@RequestMapping("api/{universe}/upgrades")
public class UpgradeService extends RepositoryServiceBase<Upgrade, UpgradeRepository> {

    public UpgradeService(@Autowired UpgradeRepository repository) {
        super(repository);
    }
}
