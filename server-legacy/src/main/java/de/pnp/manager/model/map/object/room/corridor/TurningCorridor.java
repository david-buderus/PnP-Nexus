package de.pnp.manager.model.map.object.room.corridor;

import de.pnp.manager.model.map.RotationPoint;
import de.pnp.manager.model.map.SeededRandom;
import de.pnp.manager.model.map.object.MapObjectPart;
import de.pnp.manager.model.map.object.room.Passage;
import de.pnp.manager.model.map.specification.texture.TextureHandler;
import de.pnp.manager.ui.map.IMapCanvas;

public class TurningCorridor extends AbstractCorridor {

    public TurningCorridor(SeededRandom random) {
        super(
                random,
                new MapObjectPart(0, 0, 0, 2, 1, 1),
                new MapObjectPart(0, 0, 1, 1, 1, 1)
        );
        addPassage(new Passage(this, new RotationPoint(1, 0, 0, 3)));
        addPassage(new Passage(this, new RotationPoint(0, 0, 1, 0)));
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        canvas.drawPerspectiveImage(textureHandler.getTurningCorridor(), this);
        this.drawWalls(canvas, textureHandler);
    }
}
