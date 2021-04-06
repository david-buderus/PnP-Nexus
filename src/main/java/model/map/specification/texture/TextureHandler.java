package model.map.specification.texture;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class TextureHandler {

    protected Image corridor = DEFAULT_IMAGE;
    protected Image turningCorridor = DEFAULT_IMAGE;
    protected Image corridorCrossing = DEFAULT_IMAGE;
    protected Image stairs = DEFAULT_IMAGE;
    protected Image room = DEFAULT_IMAGE;
    protected Image wall = DEFAULT_IMAGE;
    protected Image chest = DEFAULT_IMAGE;
    protected Image coffin = DEFAULT_IMAGE;

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

    public Image getChest() {
        return chest;
    }

    public Image getCoffin() {
        return coffin;
    }

    // -- STATIC -- //
    private static final Image DEFAULT_IMAGE = createDefaultImage();

    private static Image createDefaultImage() {
        int width = 100;
        int height = 100;

        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                writer.setColor(x, y, Color.BLACK);
            }
        }

        return image;
    }
}
