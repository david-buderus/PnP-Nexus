package de.pnp.manager.model.map.specification;

import de.pnp.manager.model.map.SeededRandom;
import de.pnp.manager.model.map.object.IPosition;
import de.pnp.manager.model.map.object.loot.LootObject;

public interface LootObjectSupplier {

    LootObject get(SeededRandom random, IPosition parent, int offsetX, int offsetY, int offsetZ, String container);
}
