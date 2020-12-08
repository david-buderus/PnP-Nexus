package model.map.object.room.room;

import model.map.RotationPoint;
import model.map.object.loot.Chest;
import model.map.object.loot.LootObject;
import model.map.object.room.Lootable;
import model.map.object.room.RoomObject;
import model.map.object.room.SimpleRoomObject;
import model.map.specification.MapSpecification;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Room extends SimpleRoomObject implements Lootable {

    public Room() {
        super(5, 5);
        registerEntryWithExit(new RotationPoint(2, 0, 0, 2));
        registerEntryWithExit(new RotationPoint(4, 0, 2, 3));
        registerEntryWithExit(new RotationPoint(2, 0, 4, 0));
        registerEntryWithExit(new RotationPoint(0, 0, 2, 1));
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        canvas.drawPerspectiveImage(textureHandler.getRoom(), this);
        this.drawWalls(canvas, textureHandler);
    }

    @Override
    public Optional<RoomObject> getFollowingRoomObject(MapSpecification specification, int width) {
        if (random.nextDouble() < 0.5) {
            return specification.getPossibleCorridor(width);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Collection<LootObject> generateLoot() {
        ArrayList<LootObject> result = new ArrayList<>();
        result.add(new Chest(this, 1, 0, 1));
        return result;
    }

    @Override
    public boolean preventsDeadEnd() {
        return true;
    }
}
