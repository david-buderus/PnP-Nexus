package de.pnp.manager.server.service.item;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.google.common.base.Preconditions;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemTypeTranslation;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.security.UniverseWrite;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import de.pnp.manager.server.database.item.ItemTypeTranslationRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service to access {@link ItemTypeRepository}.
 */
@RestController
@RequestMapping("api/{universe}/extended-item-types")
public class ExtendedItemTypeService {

    @Autowired
    private ItemTypeRepository typeRepository;

    @Autowired
    private ItemTypeTranslationRepository translationRepository;

    @GetMapping
    @UniverseRead
    @Operation(summary = "Get all objects from the database", operationId = "getAllExtendedItemTypes")
    public Collection<ExtendedItemType> getAll(@PathVariable String universe,
        @RequestParam(required = false) List<ObjectId> ids) {
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

    @PostMapping
    @UniverseWrite
    @Operation(summary = "Inserts the objects into the database", operationId = "insertAllExtendedItemTypes")
    public Collection<ExtendedItemType> insertAll(@PathVariable String universe,
        @RequestBody List<@Valid ExtendedItemType> itemTypes) {

        Collection<ItemType> persistedItemTypes = typeRepository.insertAll(universe,
            itemTypes.stream().map(ExtendedItemType::asItemType).toList());

        Map<ItemType, ItemTypeTranslation> persistedTranslations = translationRepository.insertAll(universe,
            itemTypes.stream().filter(type -> !type.getBroaderVariants().isEmpty()).map(ExtendedItemType::asTranslation)
                .toList()).stream().collect(Collectors.toMap(ItemTypeTranslation::getType, translation -> translation));

        return persistedItemTypes.stream().map(type -> ExtendedItemType.from(type, persistedTranslations.get(type)))
            .toList();
    }

    @PutMapping("{id}")
    @UniverseWrite
    @Operation(summary = "Updates an object in the database", operationId = "updateExtendedItemType")
    public ExtendedItemType update(@PathVariable String universe, @PathVariable ObjectId id,
        @RequestBody @Valid ExtendedItemType itemType) {
        if (itemType.getId() != null && !Objects.equals(id, itemType.getId())) {
            throw new ResponseStatusException(BAD_REQUEST, "The id of the object does not match.");
        }
        ItemType updatedItemType = typeRepository.update(universe, itemType.asItemType());

        if (itemType.getBroaderVariants().isEmpty()) {
            if (itemType.getTranslationId() != null) {
                translationRepository.remove(universe, itemType.getTranslationId());
            }
            return ExtendedItemType.from(updatedItemType, null);
        } else {
            ItemTypeTranslation updatedTranslation = translationRepository.update(universe, itemType.asTranslation());

            return ExtendedItemType.from(updatedItemType, updatedTranslation);
        }
    }

    /**
     * Combines an {@link ItemType} with it's {@link ItemTypeTranslation}
     */
    public static class ExtendedItemType extends ItemType {

        /**
         * The id of the linked translation.
         */
        private final ObjectId translationId;

        /**
         * The broader variants of this type.
         */
        @DBRef
        @NotNull
        private final Collection<ItemType> broaderVariants;

        private ExtendedItemType(ItemType baseType, ObjectId translationId,
            Set<ItemType> broaderVariants) {
            super(baseType.getId(), baseType.getName(), baseType.getTypeRestriction());
            this.translationId = translationId;
            this.broaderVariants = broaderVariants;
        }

        public ObjectId getTranslationId() {
            return translationId;
        }

        public Collection<ItemType> getBroaderVariants() {
            return broaderVariants;
        }

        /**
         * Returns the base version of the extended item type.
         */
        public ItemType asItemType() {
            return new ItemType(getId(), getName(), getTypeRestriction());
        }

        /**
         * Returns the {@link ItemTypeTranslation} of the extended type.
         */
        public ItemTypeTranslation asTranslation() {
            return new ItemTypeTranslation(getTranslationId(), asItemType(), new HashSet<>(getBroaderVariants()));
        }

        /**
         * Creates an {@link ExtendedItemType} from the given values.
         */
        private static ExtendedItemType from(ItemType itemType, ItemTypeTranslation translation) {
            if (translation == null) {
                return new ExtendedItemType(itemType, null, Set.of());
            }

            Preconditions.checkArgument(itemType.equals(translation.getType()));
            return new ExtendedItemType(itemType, translation.getId(),
                translation.getBroaderVariants());
        }
    }
}
