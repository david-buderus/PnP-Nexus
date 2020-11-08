package model.map.specification.texture;

import javafx.scene.image.Image;

public abstract class TextureHandler {

    protected Image corridor;
    protected Image turningCorridor;
    protected Image corridorCrossing;
    protected Image stairs;
    protected Image room;
    protected Image wall;

    public Image getCorridor() {
        return corridor;
    }

    public Image getTurningCorridor() {
        return turningCorridor;
    }

    public Image getCorridorCrossing() {
        return corridorCrossing;
    }

    public Image getStairs() {
        return stairs;
    }

    public Image getRoom() {
        return room;
    }

    public Image getWall() {
        return wall;
    }
}
