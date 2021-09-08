package model.map.object.loot;

import model.map.SeededRandom;
import model.map.object.IPosition;
import model.map.object.MapObjectPart;

public abstract class SimpleLootObject extends LootObject {

    protected final int offsetX, offsetY, offfsetZ;

    protected SimpleLootObject(SeededRandom random, IPosition parent, int offsetX, int offsetY, int offsetZ, int width, int depth, String container) {
        this(random, parent, offsetX, offsetY, offsetZ, width, 1, depth, container);
    }

    protected SimpleLootObject(SeededRandom random, IPosition parent, int offsetX, int offsetY, int offsetZ, int width, int height, int depth, String container) {
        super(random, container, parent, new MapObjectPart(offsetX, offsetY, offsetZ, width, height, depth));
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offfsetZ = offsetZ;
    }
}