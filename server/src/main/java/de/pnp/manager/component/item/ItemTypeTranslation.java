package de.pnp.manager.component.item;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.server.database.ItemTypeTranslationRepository;
import java.util.Objects;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A description that indicates that the {@link Item#getType() type} or {@link Item#getSubtype() subtype} is a more
 * specific variant of another type or subtype. It can be the more specific variant of multiple other types.
 */
@Document(ItemTypeTranslationRepository.REPOSITORY_NAME)
public class ItemTypeTranslation extends DatabaseObject {

    /**
     * The type of this translation.
     */
    @Indexed(unique = true)
    @DBRef
    private final ItemType type;

    /**
     * The broader variants of this type.
     */
    @DBRef
    private final Set<ItemType> broaderVariants;

    public ItemTypeTranslation(ObjectId id, ItemType type,
        Set<ItemType> broaderVariants) {
        super(id);
        this.type = type;
        this.broaderVariants = broaderVariants;
    }

    public ItemType getType() {
        return type;
    }

    public Set<ItemType> getBroaderVariants() {
        return broaderVariants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemTypeTranslation that = (ItemTypeTranslation) o;
        return Objects.equals(getType(), that.getType()) && Objects.equals(
            getBroaderVariants(), that.getBroaderVariants());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getBroaderVariants());
    }
}
