package de.pnp.manager.server.service;

import de.pnp.manager.component.item.Material;
import de.pnp.manager.server.database.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link MaterialRepository}.
 */
@RestController
@RequestMapping("{universe}/materials")
public class MaterialService extends RepositoryServiceBase<Material, MaterialRepository> {
    
    protected MaterialService(@Autowired MaterialRepository repository) {
        super(repository);
    }
}
