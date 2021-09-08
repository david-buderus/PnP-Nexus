package model.map.object.room;

import model.map.SeededRandom;
import model.map.object.MapObjectPart;

public abstract class SimpleRoomObject extends RoomObject {

    protected final int width, height, depth;

    protected SimpleRoomObject(SeededRandom random, int width, int depth) {
        this(random, width, 1, depth);
    }

    protected SimpleRoomObject(SeededRandom random, int width, int height, int depth) {
        super(random, new MapObjectPart(width, height, depth));
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getDepth() {
        return depth;
    }
}
