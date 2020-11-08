package model.map.object.room;

import model.map.object.loot.LootObject;

import java.util.Collection;

public interface Lootable {

    /** Creates a collection of LootObjects */
    Collection<LootObject> generateLoot();
}
