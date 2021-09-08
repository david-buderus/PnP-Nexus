package model.map.object.room.room;

import model.map.RotationPoint;
import model.map.SeededRandom;
import model.map.object.room.Passage;
import model.map.object.room.RoomObject;
import model.map.object.room.SimpleRoomObject;
import model.map.specification.MapSpecification;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

import java.util.Optional;

public class Entrance extends SimpleRoomObject {

    public Entrance(SeededRandom random) {
        super(random, 2, 3);
        this.addPassage(new Passage(this, new RotationPoint(1, 0, 1, 3)));
        this.deadEnd = false;
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        canvas.drawRectangle(x, y, z, width, depth, rotation);
    }

    @Override
    public Optional<RoomObject> getFollowingRoomObject(MapSpecification specification, int width) {
        return specification.getPossibleCorridor(width);
    }
}
