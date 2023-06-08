package de.pnp.manager.model.map.object.room.corridor;

import de.pnp.manager.model.map.RotationPoint;
import de.pnp.manager.model.map.SeededRandom;
import de.pnp.manager.model.map.object.room.Passage;
import de.pnp.manager.model.map.specification.texture.TextureHandler;
import de.pnp.manager.ui.map.IMapCanvas;

public class CorridorCrossing extends AbstractSimpleCorridor {

    public CorridorCrossing(SeededRandom random) {
        super(random, 1, 1);
        addPassage(new Passage(this, new RotationPoint(0, 0, 0, 0)));
        addPassage(new Passage(this, new RotationPoint(0, 0, 0, 1)));
        addPassage(new Passage(this, new RotationPoint(0, 0, 0, 2)));
        addPassage(new Passage(this, new RotationPoint(0, 0, 0, 3)));
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        canvas.drawPerspectiveImage(textureHandler.getCorridorCrossing(), this);
        this.drawWalls(canvas, textureHandler);
    }
}
