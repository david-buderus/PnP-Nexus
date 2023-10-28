package de.pnp.manager.server.service.item;

import de.pnp.manager.component.item.ItemTypeTranslation;
import de.pnp.manager.server.database.item.ItemTypeTranslationRepository;
import de.pnp.manager.server.service.RepositoryServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link ItemTypeTranslationRepository}.
 */
@RestController
@RequestMapping("api/{universe}/item-type-translations")
public class ItemTypeTranslationService extends
    RepositoryServiceBase<ItemTypeTranslation, ItemTypeTranslationRepository> {

    protected ItemTypeTranslationService(@Autowired ItemTypeTranslationRepository repository) {
        super(repository);
    }
}
