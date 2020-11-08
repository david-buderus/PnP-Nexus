package model.map.object.room.corridor;

import model.map.RotationPoint;
import model.map.object.room.RoomObject;
import model.map.object.room.SimpleRoomObject;
import model.map.specification.MapSpecification;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

import java.util.Optional;

public class CorridorCrossing extends SimpleRoomObject {

    public CorridorCrossing() {
        super(1, 1);
        registerEntryWithExit(new RotationPoint(0, 0, 0, 0));
        registerEntryWithExit(new RotationPoint(0, 0, 0, 1));
        registerEntryWithExit(new RotationPoint(0, 0, 0, 2));
        registerEntryWithExit(new RotationPoint(0, 0, 0, 3));
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        canvas.drawPerspectiveImage(textureHandler.getCorridorCrossing(), this);
        this.drawWalls(canvas, textureHandler);
    }

    @Override
    public Optional<RoomObject> getFollowingRoomObject(MapSpecification specification, int width) {
        return specification.getPossibleCorridor(width);
    }
}
