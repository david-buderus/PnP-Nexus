package model.map.specification;

import model.map.SeededRandom;
import model.map.object.room.corridor.Corridor;
import model.map.object.room.corridor.CorridorCrossing;
import model.map.object.room.corridor.Stairs;
import model.map.object.room.corridor.TurningCorridor;
import model.map.object.room.room.Room;
import model.map.specification.texture.CryptTexture;

import java.util.Random;

public class CryptSpecification extends MapSpecification {

    public CryptSpecification(SeededRandom random) {
        super(new CryptTexture(), "Krypta", random);

        //Corridors
        this.registerRoomObject(corridorFactoryMap, 4, () -> new Corridor(random, 3 + random.getRandom().nextInt(3)));
        this.registerRoomObject(corridorFactoryMap, 2, () -> new TurningCorridor(random));
        this.registerRoomObject(corridorFactoryMap, 1, () -> new Stairs(random));

        //Rooms
        this.registerRoomObject(roomFactoryMap, 2, () -> new Room(random));

        //Crossings
        this.registerRoomObject(crossingFactoryMap, 2, () -> new CorridorCrossing(random));
    }
}
