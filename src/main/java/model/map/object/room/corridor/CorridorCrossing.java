package model.map.object.room.corridor;

import model.map.RotationPoint;
import model.map.SeededRandom;
import model.map.object.room.Passage;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

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
