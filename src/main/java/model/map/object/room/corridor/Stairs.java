package model.map.object.room.corridor;

import model.map.RotationPoint;
import model.map.object.MapObjectPart;
import model.map.object.room.RoomObject;
import model.map.specification.MapSpecification;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

import java.util.Optional;

public class Stairs extends RoomObject {

    public Stairs() {
        super(
                new MapObjectPart(2, 1, 1),
                new MapObjectPart(1, 1, 0, 2, 1, 1)
        );
        this.registerEntryWithExit(new RotationPoint(2, 1, 0, 3));
        this.registerEntryWithExit(new RotationPoint(0, 0, 0, 1));
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        canvas.drawPerspectiveImage(textureHandler.getStairs(), this);
    }

    @Override
    public Optional<RoomObject> getFollowingRoomObject(MapSpecification specification, int width) {
        return specification.getPossibleCorridor(width);
    }
}
