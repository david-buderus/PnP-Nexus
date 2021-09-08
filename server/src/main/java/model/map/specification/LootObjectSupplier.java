package model.map.specification;

import model.map.SeededRandom;
import model.map.object.IPosition;
import model.map.object.loot.LootObject;

public interface LootObjectSupplier {

    LootObject get(SeededRandom random, IPosition parent, int offsetX, int offsetY, int offsetZ, String container);
}
