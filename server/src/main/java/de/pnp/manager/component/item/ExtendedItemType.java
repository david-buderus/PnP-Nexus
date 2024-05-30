package de.pnp.manager.component.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Combines an {@link ItemType} with it's {@link ItemTypeTranslation}
 */
public class ExtendedItemType extends ItemType {

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

    @JsonCreator
    public ExtendedItemType(ObjectId id, String name, ETypeRestriction typeRestriction, ObjectId translationId,
        Collection<ItemType> broaderVariants) {
        super(id, name, typeRestriction);
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
    public static ExtendedItemType from(ItemType itemType, ItemTypeTranslation translation) {
        if (translation == null) {
            return new ExtendedItemType(itemType, null, Set.of());
        }

        Preconditions.checkArgument(itemType.equals(translation.getType()));
        return new ExtendedItemType(itemType, translation.getId(),
            translation.getBroaderVariants());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ExtendedItemType that = (ExtendedItemType) o;
        return Objects.equals(getBroaderVariants(), that.getBroaderVariants());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTranslationId(), getBroaderVariants());
    }

    @Override
    public String toString() {
        return "ExtendedItemType{" +
            "name='" + getName() + '\'' +
            ", typeRestriction=" + getTypeRestriction() +
            ", broaderVariants=" + broaderVariants +
            "}";
    }
}
