package de.pnp.manager.component.item;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.server.database.item.ItemTypeRepository;
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
    @Indexed(unique = true)
    private final String name;

    /**
     * The {@link TypeRestriction} of this type.
     */
    private final TypeRestriction typeRestriction;

    public ItemType(ObjectId id, String name, TypeRestriction typeRestriction) {
        super(id);
        this.name = name;
        this.typeRestriction = typeRestriction;
    }

    @Override
    public String getName() {
        return name;
    }

    public TypeRestriction getTypeRestriction() {
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
    public enum TypeRestriction {
        ITEM(Item.class), EQUIPMENT(EquipableItem.class), JEWELLERY(Jewellery.class),
        WEAPON(Weapon.class), ARMOR(Armor.class), SHIELD(Shield.class);

        private final Class<? extends Item> correlatingClass;

        TypeRestriction(Class<? extends Item> correlatingClass) {
            this.correlatingClass = correlatingClass;
        }

        /**
         * Returns whether the restriction does not restrict on the given {@link Item}.
         */
        public boolean applicableOn(Item item) {
            return correlatingClass.isInstance(item);
        }
    }
}
