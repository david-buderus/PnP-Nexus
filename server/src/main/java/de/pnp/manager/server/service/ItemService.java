package de.pnp.manager.server.service;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.database.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link ItemRepository}.
 */
@RestController
@RequestMapping("{universe}/items")
public class ItemService extends IUniquelyNamedRepositoryServiceBase<Item, ItemRepository> {

    protected ItemService(@Autowired ItemRepository repository) {
        super(repository);
    }
}
