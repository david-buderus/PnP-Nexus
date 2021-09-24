package de.pnp.manager.model.other;

import de.pnp.manager.model.character.IInventory;

public interface IContainer {

    String getName();

    String getInventoryID();

    IInventory getInventory();
}
