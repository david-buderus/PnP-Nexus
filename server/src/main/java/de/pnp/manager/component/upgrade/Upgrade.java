package de.pnp.manager.component.upgrade;

import com.google.common.base.MoreObjects;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.TagRequirement;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.upgrade.effect.ItemEffect;
import de.pnp.manager.server.database.upgrade.UpgradeRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Objects;

/**
 * A concrete upgrade of an {@link EquipableItem} in a universe.
 */
@Document(UpgradeRepository.REPOSITORY_NAME)
public class Upgrade extends DatabaseObject {

    /**
     * The name of this upgrade.
     */
    @NotBlank
    private final String name;

    /**
     * The {@link EUpgradeRestriction} on which this {@link Upgrade} can be used.
     */
    @NotNull
    private final EUpgradeRestriction restriction;

    @NotNull
    private final TagRequirement tagRequirement;

    /**
     * The amount of {@link EquipableItem#getUpgradeSlots() slots} needed for an {@link EquipableItem} to hold this
     * upgrade.
     */
    @NotNull
    @PositiveOrZero
    private final int slots;

    /**
     * The average price of this item.
     */
    @NotNull
    @PositiveOrZero
    protected final int vendorPrice;

    /**
     * The {@link ItemEffect effects} oft this upgrade.
     */
    @NotEmpty
    private final Collection<@Valid ItemEffect> effects;

    public Upgrade(ObjectId id, String name, EUpgradeRestriction restriction, TagRequirement tagRequirement, int slots,
                   int vendorPrice,
                   Collection<ItemEffect> effects) {
        super(id);
        this.name = name;
        this.restriction = restriction;
        this.tagRequirement = tagRequirement;
        this.slots = slots;
        this.vendorPrice = vendorPrice;
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public EUpgradeRestriction getRestriction() {
        return restriction;
    }

    public int getSlots() {
        return slots;
    }

    public int getVendorPrice() {
        return vendorPrice;
    }

    public Collection<ItemEffect> getEffects() {
        return effects;
    }

    public TagRequirement getTagRequirement() {
        return tagRequirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Upgrade upgrade = (Upgrade) o;
        return getSlots() == upgrade.getSlots() && getVendorPrice() == upgrade.getVendorPrice() && Objects.equals(
                getName(), upgrade.getName()) && getRestriction() == upgrade.getRestriction() && Objects.equals(
                getTagRequirement(), upgrade.getTagRequirement()) && Objects.equals(getEffects(), upgrade.getEffects());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getRestriction(), getTagRequirement(), getSlots(), getVendorPrice(),
                getEffects());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("restriction", restriction)
                .add("tagRequirement", tagRequirement)
                .add("slots", slots)
                .add("vendorPrice", vendorPrice)
                .add("effects", effects)
                .toString();
    }
}
