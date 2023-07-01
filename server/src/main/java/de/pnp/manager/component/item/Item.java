package de.pnp.manager.component.item;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.component.item.interfaces.IItem;
import de.pnp.manager.server.database.item.ItemRepository;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A concrete item in the universe.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = Weapon.class),
    @JsonSubTypes.Type(value = Shield.class),
    @JsonSubTypes.Type(value = Armor.class),
    @JsonSubTypes.Type(value = Jewellery.class),
})
@Document(ItemRepository.REPOSITORY_NAME)
public class Item extends DatabaseObject implements IItem, IUniquelyNamedDataObject {

    /**
     * The human-readable name of this item.
     * <p>
     * This entry is always unique.
     */
    @Indexed(unique = true)
    protected final String name;

    /**
     * The type of this item.
     * <p>
     * An example would be material.
     */
    @DBRef
    protected final ItemType type;

    /**
     * The subtype of this item.
     * <p>
     * An example would be metal.
     */
    @DBRef
    protected final ItemType subtype;

    /**
     * The requirement needed to use this item.
     */
    protected final String requirement;

    /**
     * The effect of this item.
     */
    protected final String effect;

    /**
     * The rarity of this item.
     */
    protected final ERarity rarity;

    /**
     * The average price of this item.
     */
    protected final int vendorPrice;

    /**
     * The tier of this item. A higher tier indicates a better item.
     */
    protected final int tier;

    /**
     * A description of this item.
     */
    protected final String description;

    /**
     * Any kind of additional information.
     */
    protected final String note;

    /**
     * The maximal amount of this item that can be in one {@link ItemStack}.
     */
    protected final int maximalStackSize;

    /**
     * The minimal amount of this item that has to be in one {@link ItemStack}.
     */
    protected final int minimalStackSize;

    public Item(ObjectId id, String name, ItemType type, ItemType subtype, String requirement, String effect,
        ERarity rarity, int vendorPrice, int tier, String description, String note, int maximalStackSize,
        int minimalStackSize) {
        super(id);
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.requirement = requirement;
        this.effect = effect;
        this.rarity = rarity;
        this.vendorPrice = vendorPrice;
        this.tier = tier;
        this.description = description;
        this.note = note;
        this.maximalStackSize = maximalStackSize;
        this.minimalStackSize = minimalStackSize;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemType getType() {
        return type;
    }

    @Override
    public ItemType getSubtype() {
        return subtype;
    }

    @Override
    public String getRequirement() {
        return requirement;
    }

    @Override
    public String getEffect() {
        return effect;
    }

    @Override
    public ERarity getRarity() {
        return rarity;
    }

    @Override
    public int getVendorPrice() {
        return vendorPrice;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getNote() {
        return note;
    }

    public int getMaximalStackSize() {
        return maximalStackSize;
    }

    public int getMinimalStackSize() {
        return minimalStackSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return getVendorPrice() == item.getVendorPrice() && getTier() == item.getTier()
            && getMaximalStackSize() == item.getMaximalStackSize()
            && getMinimalStackSize() == item.getMinimalStackSize()
            && Objects.equals(getName(), item.getName()) && Objects.equals(getType(), item.getType())
            && Objects.equals(getSubtype(), item.getSubtype()) && Objects.equals(getRequirement(),
            item.getRequirement()) && Objects.equals(getEffect(), item.getEffect())
            && getRarity() == item.getRarity() && Objects.equals(getDescription(), item.getDescription())
            && Objects.equals(getNote(), item.getNote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getType(), getSubtype(), getRequirement(), getEffect(), getRarity(),
            getVendorPrice(), getTier(), getDescription(), getNote(), getMaximalStackSize(), getMinimalStackSize());
    }

    @Override
    public String toString() {
        return "Item{" +
            "name='" + name + '\'' +
            ", type=" + type +
            ", subtype=" + subtype +
            ", requirement='" + requirement + '\'' +
            ", effect='" + effect + '\'' +
            ", rarity=" + rarity +
            ", vendorPrice=" + vendorPrice +
            ", tier=" + tier +
            ", description='" + description + '\'' +
            ", note='" + note + '\'' +
            ", maximalStackSize=" + maximalStackSize +
            ", minimalStackSize=" + minimalStackSize +
            '}';
    }
}
