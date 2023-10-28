package de.pnp.manager.server.service.item;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.item.ItemTypeTranslation;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import de.pnp.manager.server.database.item.ItemTypeTranslationRepository;
import de.pnp.manager.server.service.RepositoryServiceBaseTest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ItemTypeTranslationService}.
 */
public class ItemTypeTranslationServiceTest extends
    RepositoryServiceBaseTest<ItemTypeTranslation, ItemTypeTranslationRepository, ItemTypeTranslationService> {

    @Autowired
    private ItemTypeRepository typeRepository;

    public ItemTypeTranslationServiceTest(@Autowired ItemTypeTranslationService itemTypeTranslationService) {
        super(itemTypeTranslationService, ItemTypeTranslation.class);
    }

    @Override
    protected List<ItemTypeTranslation> createObjects() {
        return List.of(
            new ItemTypeTranslation(null, getItemType("Sword", ETypeRestriction.WEAPON),
                Set.of(getItemType("Weapon", ETypeRestriction.WEAPON))),
            new ItemTypeTranslation(null, getItemType("Weapon", ETypeRestriction.WEAPON),
                Set.of(getItemType("Handheld", ETypeRestriction.HANDHELD),
                    getItemType("Equipment", ETypeRestriction.EQUIPMENT))),
            new ItemTypeTranslation(null, getItemType("Handheld", ETypeRestriction.HANDHELD), Set.of()),
            new ItemTypeTranslation(null, getItemType("Equipment", ETypeRestriction.EQUIPMENT), Set.of())
        );
    }

    private ItemType getItemType(String name, ETypeRestriction restriction) {
        Optional<ItemType> itemType = typeRepository.get(getUniverseName(), name);
        return itemType.orElse(typeRepository.insert(getUniverseName(), new ItemType(null, name, restriction)));
    }
}
