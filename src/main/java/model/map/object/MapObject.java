package model.map.object;

import model.map.SeededRandom;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

import java.util.Collection;
import java.util.List;

public abstract class MapObject implements IPosition {

    protected SeededRandom random;
    protected int x, y, z, rotation;
    protected final List<MapObjectPart> parts;
    protected String infoText;

    protected MapObject(SeededRandom random , MapObjectPart... parts) {
        this.rotation = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.random = random;
        this.parts = List.of(parts);
        this.infoText = "";
    }

    public abstract void draw(IMapCanvas canvas, TextureHandler textureHandler);

    public int getWidth() {
        return parts.stream().mapToInt(part -> part.getOffsetX() + part.getWidth()).max().orElse(0);
    }

    public int getDepth() {
        return parts.stream().mapToInt(part -> part.getOffsetZ() + part.getDepth()).max().orElse(0);
    }

    public int getHeight() {
        return parts.stream().mapToInt(part -> part.getOffsetY() + part.getHeight()).max().orElse(0);
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = ((rotation % 4) + 4) % 4;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setCoordinates(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Collection<MapObjectPart> getParts() {
        return parts;
    }

    public String getInfoText() {
        return infoText;
    }

    public void onDelete() { }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + getX() + ", " + getY() + ", " + getZ() + ")";
    }
}
