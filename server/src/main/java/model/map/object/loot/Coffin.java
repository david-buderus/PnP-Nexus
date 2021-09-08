package model.map.object.loot;

import model.map.SeededRandom;
import model.map.object.IPosition;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

public class Coffin extends SimpleLootObject {

    public Coffin(SeededRandom random, IPosition parent, int offsetX, int offsetY, int offsetZ, String container) {
        super(random, parent, offsetX, offsetY, offsetZ, 2, 1, container);
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        canvas.drawImage(textureHandler.getCoffin(), parent, offsetX, offsetY, offfsetZ, 2, 1, 0);
    }
}
