package de.pnp.manager.server.contoller;

import de.pnp.manager.component.item.ExtendedItemType;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemTypeTranslation;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import de.pnp.manager.server.database.item.ItemTypeTranslationRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A controller to create and manipulate {@link ExtendedItemType}.
 */
@Component
public class ExtendedItemTypeController {

    @Autowired
    private ItemTypeRepository typeRepository;

    @Autowired
    private ItemTypeTranslationRepository translationRepository;

    /**
     * Returns all {@link ExtendedItemType} of the given universe
     */
    public Collection<ExtendedItemType> getAll(String universe) {
        return getAll(universe, null);
    }

    /**
     * Returns all {@link ExtendedItemType} of the given universe matching the given ids
     */
    public Collection<ExtendedItemType> getAll(String universe, @Nullable List<ObjectId> ids) {
        Collection<ItemType> itemTypes;
        if (ids == null || ids.isEmpty()) {
            itemTypes = typeRepository.getAll(universe);
        } else {
            itemTypes = typeRepository.getAll(universe, ids);
        }

        Map<ItemType, ItemTypeTranslation> translations = translationRepository.get(universe,
            itemTypes);

        return itemTypes.stream().map(type -> ExtendedItemType.from(type, translations.get(type))).toList();
    }

    /**
     * Inserts the given {@link ExtendedItemType} into the universe.
     */
    public Collection<ExtendedItemType> insertAll(String universe, Collection<ExtendedItemType> itemTypes) {

        Collection<ItemType> persistedItemTypes = typeRepository.insertAll(universe,
            itemTypes.stream().map(ExtendedItemType::asItemType).toList());

        Map<String, ExtendedItemType> typeByName = itemTypes.stream()
            .collect(Collectors.toMap(ExtendedItemType::getName, type -> type));

        // Add the id of the persisted item types
        List<ExtendedItemType> itemTypesWithId = persistedItemTypes.stream()
            .map(type -> ExtendedItemType.from(type, typeByName.get(type.getName()).asTranslation())).toList();

        Map<ItemType, ItemTypeTranslation> persistedTranslations = translationRepository.insertAll(universe,
            itemTypesWithId.stream().filter(type -> !type.getBroaderVariants().isEmpty())
                .map(ExtendedItemType::asTranslation)
                .toList()).stream().collect(Collectors.toMap(ItemTypeTranslation::getType, translation -> translation));

        return persistedItemTypes.stream().map(type -> ExtendedItemType.from(type, persistedTranslations.get(type)))
            .toList();
    }

    /**
     * Inserts the given {@link ExtendedItemType} into the universe.
     */
    public ExtendedItemType insert(String universe, ExtendedItemType itemType) {
        return insertAll(universe, List.of(itemType)).stream().findFirst().orElseThrow();
    }

    /**
     * Updates the given {@link ExtendedItemType}
     */
    public ExtendedItemType update(String universe, ExtendedItemType itemType) {
        ItemType updatedItemType = typeRepository.update(universe, itemType.asItemType());

        if (itemType.getBroaderVariants().isEmpty()) {
            if (itemType.getTranslationId() != null) {
                translationRepository.remove(universe, itemType.getTranslationId());
            }
            return ExtendedItemType.from(updatedItemType, null);
        } else if (itemType.getTranslationId() != null) {
            ItemTypeTranslation updatedTranslation = translationRepository.update(universe, itemType.asTranslation());

            return ExtendedItemType.from(updatedItemType, updatedTranslation);
        } else {
            ItemTypeTranslation newTranslation = translationRepository.insert(universe, itemType.asTranslation());

            return ExtendedItemType.from(updatedItemType, newTranslation);
        }
    }
}
