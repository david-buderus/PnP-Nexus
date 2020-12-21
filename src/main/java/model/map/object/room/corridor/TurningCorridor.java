package model.map.object.room.corridor;

import model.map.RotationPoint;
import model.map.SeededRandom;
import model.map.object.MapObjectPart;
import model.map.object.room.Passage;
import model.map.object.room.RoomObject;
import model.map.specification.MapSpecification;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

import java.util.Optional;

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
