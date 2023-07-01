package de.pnp.manager.component.item.interfaces;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.ItemType;

/**
 * An item in the universe.
 */
public interface IItem {

    String getName();

    ItemType getType();

    ItemType getSubtype();

    String getRequirement();

    String getEffect();

    ERarity getRarity();

    int getVendorPrice();

    int getTier();

    String getDescription();

    String getNote();
}

