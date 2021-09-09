package de.pnp.manager.model.map.object.loot;

import de.pnp.manager.model.map.SeededRandom;
import de.pnp.manager.model.map.object.IPosition;
import de.pnp.manager.model.map.specification.texture.TextureHandler;
import de.pnp.manager.ui.map.IMapCanvas;

public class Coffin extends SimpleLootObject {

    public Coffin(SeededRandom random, IPosition parent, int offsetX, int offsetY, int offsetZ, String container) {
        super(random, parent, offsetX, offsetY, offsetZ, 2, 1, container);
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        canvas.drawImage(textureHandler.getCoffin(), parent, offsetX, offsetY, offfsetZ, 2, 1, 0);
    }
}
