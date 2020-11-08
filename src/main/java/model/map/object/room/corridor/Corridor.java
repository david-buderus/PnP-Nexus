package model.map.object.room.corridor;

import model.map.RotationPoint;
import model.map.object.room.RoomObject;
import model.map.object.room.SimpleRoomObject;
import model.map.specification.MapSpecification;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

import java.util.Optional;

public class Corridor extends SimpleRoomObject {

    protected final int length;

    public Corridor(int length) {
        super(length, 1);
        this.length = length;
        this.registerEntryWithExit(new RotationPoint(length - 1, 0, 0, 3));
        this.registerEntryWithExit(new RotationPoint(0, 0, 0, 1));
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

    @Override
    public Optional<RoomObject> getFollowingRoomObject(MapSpecification specification, int width) {
        double d = random.nextDouble();
        if (d < 0.25) {
            return specification.getPossibleRoom(width);
        } else if (d < 0.5) {
            return specification.getPossibleCrossings(width);
        } else {
            return specification.getPossibleCorridor(width);
        }
    }
}
