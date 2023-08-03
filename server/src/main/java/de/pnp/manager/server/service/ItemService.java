package de.pnp.manager.server.service;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.database.item.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link ItemRepository}.
 */
@RestController
@RequestMapping("api/{universe}/items")
public class ItemService extends RepositoryServiceBase<Item, ItemRepository> {

    protected ItemService(@Autowired ItemRepository repository) {
        super(repository);
    }
}
