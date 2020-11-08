package model.map.object.loot;

import javafx.scene.image.Image;
import model.map.object.IPosition;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

public class Chest extends SimpleLootObject {

    public Chest(IPosition parent, int offsetX, int offsetY, int offsetZ) {
        super(parent, offsetX, offsetY, offsetZ, 1, 1, "Schatztruhe");
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        Image image = new Image("Icon.png");
        canvas.drawImage(image, parent, offsetX, offsetY, offfsetZ, 1, 1, rotation);
    }
}
