package de.pnp.manager.model.map.object;

public class MapObjectPart {

    protected final int offsetX, offsetY, offsetZ;
    protected final int width, height, depth;

    public MapObjectPart(int offsetX, int offsetY, int offsetZ, int width, int height, int depth) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public MapObjectPart(int width, int height, int depth) {
        this(0, 0, 0, width, height, depth);
    }

    public MapObjectPart(int width, int depth) {
        this(width, 1, depth);
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetZ() {
        return offsetZ;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }
}
