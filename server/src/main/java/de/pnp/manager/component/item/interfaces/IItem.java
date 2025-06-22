package de.pnp.manager.component.item.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.Tag;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;

import java.util.Set;

/**
 * An item in the universe.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = Weapon.class, name = "Weapon"),
        @JsonSubTypes.Type(value = Shield.class, name = "Shield"),
        @JsonSubTypes.Type(value = Armor.class, name = "Armor"),
        @JsonSubTypes.Type(value = Jewellery.class, name = "Jewellery"),
        @JsonSubTypes.Type(value = Item.class, name = "Item")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface IItem {

    /**
     * The human-readable name of this item.
     */
    String getName();

    /**
     * The tags of this item.
     */
    Set<Tag> getTags();

    /**
     * The requirement needed to use this item.
     */
    String getRequirement();

    /**
     * The effect of this item.
     */
    String getEffect();

    /**
     * The rarity of this item.
     */
    ERarity getRarity();

    /**
     * The average price of this item.
     */
    int getVendorPrice();

    /**
     * The tier of this item. A higher tier indicates a better item.
     */
    int getTier();

    /**
     * A description of this item.
     */
    String getDescription();

    /**
     * Any kind of additional information.
     */
    String getNote();

    /**
     * The maximum amount of this item that can be contained in one {@link ItemStack}.
     */
    int getMaximumStackSize();

    /**
     * The minimum amount of this item that has to be contained in one {@link ItemStack}.
     * <p>
     * A {@code minimumStackSize} of {@code 0} is interpreted as minimum stackSize {@code > 0}.
     */
    int getMinimumStackSize();
}

