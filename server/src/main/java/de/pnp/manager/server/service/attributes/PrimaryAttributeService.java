package de.pnp.manager.server.service.attributes;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.service.RepositoryServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link PrimaryAttributeRepository}.
 */
@RestController
@RequestMapping("{universe}/primary-attributes")
public class PrimaryAttributeService extends RepositoryServiceBase<PrimaryAttribute, PrimaryAttributeRepository> {

    protected PrimaryAttributeService(@Autowired PrimaryAttributeRepository repository) {
        super(repository);
    }
}
