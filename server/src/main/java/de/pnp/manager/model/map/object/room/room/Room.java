package de.pnp.manager.model.map.object.room.room;

import de.pnp.manager.model.map.RotationPoint;
import de.pnp.manager.model.map.SeededRandom;
import de.pnp.manager.model.map.object.loot.LootObject;
import de.pnp.manager.model.map.object.loot.LootObjectType;
import de.pnp.manager.model.map.object.room.Passage;
import de.pnp.manager.model.map.object.room.RoomObject;
import de.pnp.manager.model.map.object.room.SimpleRoomObject;
import de.pnp.manager.model.map.object.room.WithLootables;
import de.pnp.manager.model.map.specification.MapSpecification;
import de.pnp.manager.model.map.specification.texture.TextureHandler;
import de.pnp.manager.ui.map.IMapCanvas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Room extends SimpleRoomObject implements WithLootables {

    public Room(SeededRandom random) {
        super(random, 5, 5);
        this.deadEnd = false;
        addPassage(new Passage(this, new RotationPoint(2, 0, 4, 0), true));
        addPassage(new Passage(this, new RotationPoint(0, 0, 2, 1), true));
        addPassage(new Passage(this, new RotationPoint(2, 0, 0, 2), true));
        addPassage(new Passage(this, new RotationPoint(4, 0, 2, 3), true));
    }

    @Override
    public void draw(IMapCanvas canvas, TextureHandler textureHandler) {
        canvas.drawPerspectiveImage(textureHandler.getRoom(), this);
        this.drawWalls(canvas, textureHandler);
    }

    @Override
    public Optional<RoomObject> getFollowingRoomObject(MapSpecification specification, int width) {
        if (random.getRandom().nextDouble() < 0.5) {
            return specification.getPossibleCorridor(width);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Collection<LootObject> generateLootables(MapSpecification specification) {
        ArrayList<LootObject> result = new ArrayList<>();
        specification.getLootObject(LootObjectType.chest, random,this, 1, 0, 1)
                .ifPresent(result::add);
        specification.getLootObject(LootObjectType.coffin, random,this, 3, 0, 1)
                .ifPresent(result::add);
        return result;
    }
}