package model.map.object.room;

import model.map.object.loot.LootObject;
import model.map.specification.MapSpecification;

import java.util.Collection;
import java.util.Collections;

public interface Lootable {

    /**
     * Creates a collection of LootObjects
     */
    default Collection<LootObject> generateLoot(MapSpecification specification) {
        return Collections.emptyList();
    }
}
