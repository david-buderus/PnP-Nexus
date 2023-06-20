package de.pnp.manager.model.map.object.room.corridor;

import de.pnp.manager.model.map.SeededRandom;
import de.pnp.manager.model.map.object.MapObjectPart;
import de.pnp.manager.model.map.object.room.RoomObject;
import de.pnp.manager.model.map.specification.MapSpecification;

import java.util.Optional;

public abstract class AbstractCorridor extends RoomObject {

    protected AbstractCorridor(SeededRandom random, MapObjectPart... parts) {
        super(random, parts);
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
