package de.pnp.manager.server.service.attributes;

import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import de.pnp.manager.server.service.RepositoryServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link SecondaryAttributeRepository}.
 */
@RestController
@RequestMapping("{universe}/secondary-attributes")
public class SecondaryAttributeService extends RepositoryServiceBase<SecondaryAttribute, SecondaryAttributeRepository> {

    protected SecondaryAttributeService(@Autowired SecondaryAttributeRepository repository) {
        super(repository);
    }
}
