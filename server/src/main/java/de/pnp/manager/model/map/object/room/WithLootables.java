package de.pnp.manager.model.map.object.room;

import de.pnp.manager.model.map.object.loot.LootObject;
import de.pnp.manager.model.map.specification.MapSpecification;

import java.util.Collection;
import java.util.Collections;

public interface WithLootables {

    /**
     * Creates a collection of LootObjects
     */
    default Collection<LootObject> generateLootables(MapSpecification specification) {
        return Collections.emptyList();
    }
}
