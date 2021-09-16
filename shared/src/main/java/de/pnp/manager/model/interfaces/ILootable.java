package de.pnp.manager.model.interfaces;

import de.pnp.manager.model.loot.ILootTable;

public interface ILootable {

    /**
     * Returns a LootTable where all items were taken into account
     */
    ILootTable getFinishedLootTable();

}
