package de.pnp.manager.component.item;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.equipable.HandheldEquipableItem;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import de.pnp.manager.component.item.interfaces.IItem;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A type or subtype of a {@link Item}.
 */
@Document(ItemTypeRepository.REPOSITORY_NAME)
public class ItemType extends DatabaseObject implements IUniquelyNamedDataObject {

    /**
     * The human-readable name of this type.
     * <p>
     * This entry is always unique.
     */
    @NotBlank
    @Indexed(unique = true)
    private final String name;

    /**
     * The {@link ETypeRestriction} of this type.
     */
    @NotNull
    private final ETypeRestriction typeRestriction;

    public ItemType(ObjectId id, String name, ETypeRestriction typeRestriction) {
        super(id);
        this.name = name;
        this.typeRestriction = typeRestriction;
    }

    @Override
    public String getName() {
        return name;
    }

    public ETypeRestriction getTypeRestriction() {
        return typeRestriction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemType type = (ItemType) o;
        return Objects.equals(getName(), type.getName())
            && getTypeRestriction() == type.getTypeRestriction();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getTypeRestriction());
    }

    @Override
    public String toString() {
        return "ItemType{" +
            "name='" + name + '\'' +
            ", typeRestriction=" + typeRestriction +
            '}';
    }

    /**
     * A restriction of a {@link ItemType}.
     */
    public enum ETypeRestriction {
        ITEM(Item.class, "item"), EQUIPMENT(EquipableItem.class, "equipment"),
        JEWELLERY(Jewellery.class, "jewellery"), WEAPON(Weapon.class, "weapon"),
        DEFENSIVE_ITEM(IDefensiveItem.class, "defensive_item"), SHIELD(Shield.class, "shield"),
        HANDHELD(HandheldEquipableItem.class, "handheld_item"), ARMOR(Armor.class, "armor");

        private final Class<? extends IItem> correlatingClass;

        private final String messageTemplate;

        ETypeRestriction(Class<? extends IItem> correlatingClass, String messageTemplate) {
            this.correlatingClass = correlatingClass;
            this.messageTemplate = messageTemplate;
        }

        /**
         * Returns whether the restriction does not restrict on the given {@link Item}.
         */
        public boolean applicableOn(Item item) {
            return correlatingClass.isInstance(item);
        }

        /**
         * The message template to localize this enum for users.
         */
        public String getMessageTemplate() {
            return messageTemplate;
        }
    }
}
