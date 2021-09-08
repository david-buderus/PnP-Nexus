package model.map.object.room.corridor;

import model.map.SeededRandom;
import model.map.object.room.RoomObject;
import model.map.object.room.SimpleRoomObject;
import model.map.specification.MapSpecification;

import java.util.Optional;

public abstract class AbstractSimpleCorridor extends SimpleRoomObject {

    protected AbstractSimpleCorridor(SeededRandom random, int width, int depth) {
        super(random, width, depth);
    }

    @Override
    public Optional<RoomObject> getFollowingRoomObject(MapSpecification specification, int width) {
        double d = random.getRandom().nextDouble();
        if (d < 0.25) {
            return specification.getPossibleRoom(width);
        } else if (d < 0.5) {
            return specification.getPossibleCrossings(width);
        } else {
            return specification.getPossibleCorridor(width);
        }
    }
}
