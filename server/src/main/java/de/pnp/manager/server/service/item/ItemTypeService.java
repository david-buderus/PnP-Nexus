package de.pnp.manager.server.service.item;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import de.pnp.manager.server.service.RepositoryServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link ItemTypeRepository}.
 */
@RestController
@RequestMapping("api/{universe}/item-types")
public class ItemTypeService extends RepositoryServiceBase<ItemType, ItemTypeRepository> {

    protected ItemTypeService(@Autowired ItemTypeRepository repository) {
        super(repository);
    }
}
