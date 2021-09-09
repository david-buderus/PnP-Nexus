package de.pnp.manager.model.map.specification;

import de.pnp.manager.model.map.SeededRandom;
import de.pnp.manager.model.map.object.loot.Chest;
import de.pnp.manager.model.map.object.loot.Coffin;
import de.pnp.manager.model.map.object.loot.LootObjectType;
import de.pnp.manager.model.map.object.room.corridor.Corridor;
import de.pnp.manager.model.map.object.room.corridor.CorridorCrossing;
import de.pnp.manager.model.map.object.room.corridor.Stairs;
import de.pnp.manager.model.map.object.room.corridor.TurningCorridor;
import de.pnp.manager.model.map.object.room.room.Room;
import de.pnp.manager.model.map.specification.texture.CryptTexture;

public class CryptSpecification extends MapSpecification {

    public CryptSpecification(SeededRandom random) {
        super(new CryptTexture(), "map.structure.crypt", random);

        //Corridors
        this.registerRoomObject(corridorFactoryMap, 4, () -> new Corridor(random, 3 + random.getRandom().nextInt(3)));
        this.registerRoomObject(corridorFactoryMap, 2, () -> new TurningCorridor(random));
        this.registerRoomObject(corridorFactoryMap, 1, () -> new Stairs(random));

        //Rooms
        this.registerRoomObject(roomFactoryMap, 2, () -> new Room(random));

        //Crossings
        this.registerRoomObject(crossingFactoryMap, 2, () -> new CorridorCrossing(random));

        //Loot
        this.registerLootObject(LootObjectType.chest, 6, "map.loot.treasureChest.small", Chest::new);
        this.registerLootObject(LootObjectType.chest, 3, "map.loot.treasureChest", Chest::new);
        this.registerLootObject(LootObjectType.chest, 1, "map.loot.treasureChest.rare", Chest::new);
        this.registerLootObject(LootObjectType.coffin, 10, "map.loot.coffin", Coffin::new);
        this.registerLootObject(LootObjectType.coffin, 1, "map.loot.coffin.rare", Coffin::new);
    }
}
