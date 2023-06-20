package de.pnp.manager.model.map.object.room.corridor;

import de.pnp.manager.model.map.RotationPoint;
import de.pnp.manager.model.map.SeededRandom;
import de.pnp.manager.model.map.object.room.Passage;
import de.pnp.manager.model.map.specification.texture.TextureHandler;
import de.pnp.manager.ui.map.IMapCanvas;

public class Corridor extends AbstractSimpleCorridor {

    protected final int length;

    public Corridor(SeededRandom random, int length) {
        super(random, length, 1);
        this.length = length;
        addPassage(new Passage(this, new RotationPoint(length - 1, 0, 0, 3)));
        addPassage(new Passage(this, new RotationPoint(0, 0, 0, 1)));
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        for (int w = 0; w < length; w++) {
            if (rotation % 2 == 0) {
                canvas.drawPerspectiveImage(textureHandler.getCorridor(), 0, 0, 100, 100, x + w, y, z, 1, 1, 1, rotation);
            } else {
                canvas.drawPerspectiveImage(textureHandler.getCorridor(), 0, 0, 100, 100, x, y, z + w, 1, 1, 1, rotation);
            }
        }
        drawWalls(canvas, textureHandler);
    }
}
