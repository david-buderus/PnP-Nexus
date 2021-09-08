package model.map.object.room;

import model.map.object.loot.LootObject;
import model.map.specification.MapSpecification;

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
