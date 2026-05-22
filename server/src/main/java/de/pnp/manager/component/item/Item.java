package de.pnp.manager.component.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.MoreObjects;
import de.pnp.manager.Tag;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.component.item.interfaces.IItem;
import de.pnp.manager.component.upgrade.effect.ItemEffect;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.validation.MatchingItemEffects;
import de.pnp.manager.validation.MatchingStackSizes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A concrete item in the universe.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = Weapon.class, name = "Weapon"),
        @JsonSubTypes.Type(value = Shield.class, name = "Shield"),
        @JsonSubTypes.Type(value = Armor.class, name = "Armor"),
        @JsonSubTypes.Type(value = Jewellery.class, name = "Jewellery"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@MatchingStackSizes
@MatchingItemEffects
@Document(ItemRepository.REPOSITORY_NAME)
public class Item extends DatabaseObject implements IItem, IUniquelyNamedDataObject {

    /**
     * The human-readable name of this item.
     * <p>
     * This entry is always unique.
     */
    @Indexed(unique = true)
    @NotBlank
    protected final String name;

    /**
     * The tags of this item.
     * <p>
     * An example would be sword.
     */
    protected final @NotNull Set<@NotNull Tag> tags;

    /**
     * The requirement needed to use this item.
     */
    @NotNull
    protected final String requirement;

    /**
     * The effect of this item.
     */
    @NotNull
    protected final List<@NotNull ItemEffect> effects;

    /**
     * The rarity of this item.
     */
    @NotNull
    protected final ERarity rarity;

    /**
     * The average price of this item.
     */
    @NotNull
    @PositiveOrZero
    protected final int vendorPrice;

    /**
     * The tier of this item. A higher tier indicates a better item.
     */
    @NotNull
    @Positive
    protected final int tier;

    /**
     * A description of this item.
     */
    @NotNull
    protected final String description;

    /**
     * Any kind of additional information.
     */
    @NotNull
    protected final String note;

    /**
     * The maximum amount of this item that can be contained in one {@link ItemStack}.
     */
    @NotNull
    @Positive
    protected final int maximumStackSize;

    /**
     * The minimum amount of this item that has to be contained in one {@link ItemStack}.
     * <p>
     * A {@code minimumStackSize} of {@code 0} is interpreted as minimum stackSize {@code > 0}.
     */
    @NotNull
    @PositiveOrZero
    protected final int minimumStackSize;

    /**
     * The amount of upgrades this item can hold.
     */
    @NotNull
    @PositiveOrZero
    protected final int upgradeSlots;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Item(ObjectId id, String name, Set<Tag> tags, String requirement, List<ItemEffect> effects, ERarity rarity,
                int vendorPrice, int tier, String description, String note, int maximumStackSize, int minimumStackSize, int upgradeSlots) {
        super(id);
        this.name = name;
        this.tags = tags;
        this.requirement = requirement;
        this.effects = effects;
        this.rarity = rarity;
        this.vendorPrice = vendorPrice;
        this.tier = tier;
        this.description = description;
        this.note = note;
        this.maximumStackSize = maximumStackSize;
        this.minimumStackSize = minimumStackSize;
        this.upgradeSlots = upgradeSlots;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * @see #tags
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public String getRequirement() {
        return requirement;
    }

    @Override
    public @NotNull List<ItemEffect> getEffects() {
        return effects;
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

    @Override
    public int getMaximumStackSize() {
        return maximumStackSize;
    }

    @Override
    public int getMinimumStackSize() {
        return minimumStackSize;
    }

    @Override
    public int getUpgradeSlots() {
        return upgradeSlots;
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
                && getMaximumStackSize() == item.getMaximumStackSize()
                && getMinimumStackSize() == item.getMinimumStackSize()
                && Objects.equals(getName(), item.getName()) && Objects.equals(getTags(), item.getTags())
                && Objects.equals(getRequirement(), item.getRequirement()) && Objects.equals(getEffects(),
                item.getEffects()) && getRarity() == item.getRarity() && Objects.equals(getDescription(),
                item.getDescription()) && Objects.equals(getNote(), item.getNote())
                && getUpgradeSlots() == item.getUpgradeSlots();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getTags(), getRequirement(), getEffects(), getRarity(), getVendorPrice(),
                getTier(), getDescription(), getNote(), getMaximumStackSize(), getMinimumStackSize());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("tags", tags)
                .add("requirement", requirement)
                .add("effects", effects)
                .add("rarity", rarity)
                .add("vendorPrice", vendorPrice)
                .add("tier", tier)
                .add("description", description)
                .add("note", note)
                .add("maximumStackSize", maximumStackSize)
                .add("minimumStackSize", minimumStackSize)
                .add("upgradeSlots", upgradeSlots)
                .toString();
    }
}
