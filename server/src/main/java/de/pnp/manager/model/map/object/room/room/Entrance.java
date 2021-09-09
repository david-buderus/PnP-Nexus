package de.pnp.manager.model.map.object.room.room;

import de.pnp.manager.model.map.RotationPoint;
import de.pnp.manager.model.map.SeededRandom;
import de.pnp.manager.model.map.object.room.Passage;
import de.pnp.manager.model.map.object.room.RoomObject;
import de.pnp.manager.model.map.object.room.SimpleRoomObject;
import de.pnp.manager.model.map.specification.MapSpecification;
import de.pnp.manager.model.map.specification.texture.TextureHandler;
import de.pnp.manager.ui.map.IMapCanvas;

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
