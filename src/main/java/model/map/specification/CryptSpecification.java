package model.map.specification;

import model.map.object.room.corridor.Corridor;
import model.map.object.room.corridor.CorridorCrossing;
import model.map.object.room.corridor.Stairs;
import model.map.object.room.corridor.TurningCorridor;
import model.map.object.room.room.Room;
import model.map.specification.texture.CryptTexture;

public class CryptSpecification extends MapSpecification {

    public CryptSpecification() {
        super(new CryptTexture(), "Krypta");

        //Corridors
        this.registerRoomObject(corridorFactoryMap, 4, () -> new Corridor(3 + random.nextInt(3)));
        this.registerRoomObject(corridorFactoryMap, 2, TurningCorridor::new);
        this.registerRoomObject(corridorFactoryMap, 1, Stairs::new);

        //Rooms
        this.registerRoomObject(roomFactoryMap, 2, Room::new);

        //Crossings
        this.registerRoomObject(crossingFactoryMap, 2, CorridorCrossing::new);
    }
}
