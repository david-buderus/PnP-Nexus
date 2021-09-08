package model.map.object.room.corridor;

import model.map.RotationPoint;
import model.map.SeededRandom;
import model.map.object.MapObjectPart;
import model.map.object.room.Passage;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

public class Stairs extends AbstractCorridor {

    public Stairs(SeededRandom random) {
        super(
                random,
                new MapObjectPart(2, 1, 1),
                new MapObjectPart(1, 1, 0, 2, 1, 1)
        );
        addPassage(new Passage(this, new RotationPoint(2, 1, 0, 3)));
        addPassage(new Passage(this, new RotationPoint(0, 0, 0, 1)));
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        canvas.drawPerspectiveImage(textureHandler.getStairs(), this);
    }
}
