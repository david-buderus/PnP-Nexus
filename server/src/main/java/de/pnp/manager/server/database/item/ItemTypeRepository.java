package de.pnp.manager.server.database.item;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.server.database.interfaces.IUniquelyNamedRepository;
import org.springframework.stereotype.Component;

/**
 * The repository for {@link ItemType types}.
 */
@Component
public class ItemTypeRepository extends RepositoryBase<ItemType> implements
    IUniquelyNamedRepository<ItemType> {

    /**
     * Name of the repository
     */
    public static final String REPOSITORY_NAME = "types";

    public ItemTypeRepository() {
        super(ItemType.class, REPOSITORY_NAME);
    }
}
