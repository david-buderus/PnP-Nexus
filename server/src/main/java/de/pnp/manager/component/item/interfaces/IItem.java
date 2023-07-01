package de.pnp.manager.component.item.interfaces;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.ItemType;

/**
 * An item in the universe.
 */
public interface IItem {

    /**
     * The human-readable name of this item.
     */
    String getName();

    /**
     * The type of this item.
     */
    ItemType getType();

    /**
     * The subtype of this item.
     */
    ItemType getSubtype();

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
    int getMaximalStackSize();

    /**
     * The minimum amount of this item that has to be contained in one {@link ItemStack}.
     */
    int getMinimalStackSize();
}

