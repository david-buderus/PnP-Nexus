package de.pnp.manager.model.map.specification;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.loot.DungeonLootFactory;
import de.pnp.manager.model.loot.LootFactory;
import de.pnp.manager.model.map.SeededRandom;
import de.pnp.manager.model.map.WeightedFactoryList;
import de.pnp.manager.model.map.WeightedLootObjectFactoryList;
import de.pnp.manager.model.map.object.IPosition;
import de.pnp.manager.model.map.object.loot.LootObject;
import de.pnp.manager.model.map.object.loot.LootObjectType;
import de.pnp.manager.model.map.object.room.RoomObject;
import de.pnp.manager.model.map.specification.texture.TextureHandler;

import java.util.*;
import java.util.function.Supplier;

import static de.pnp.manager.main.LanguageUtility.getMessage;

public abstract class MapSpecification {


    protected TextureHandler textureHandler;
    protected SeededRandom random;
    protected HashMap<Integer, WeightedFactoryList<RoomObject>> entranceFactoryMap;
    protected HashMap<Integer, WeightedFactoryList<RoomObject>> corridorFactoryMap;
    protected HashMap<Integer, WeightedFactoryList<RoomObject>> crossingFactoryMap;
    protected HashMap<Integer, WeightedFactoryList<RoomObject>> roomFactoryMap;
    protected HashMap<LootObjectType, WeightedLootObjectFactoryList> lootFactoryMap;

    protected HashMap<String, Collection<LootFactory>> lootFactoryFactoryMap;

    protected MapSpecification(TextureHandler textureHandler, String placeKey, SeededRandom random) {
        this.textureHandler = textureHandler;
        this.entranceFactoryMap = new HashMap<>();
        this.corridorFactoryMap = new HashMap<>();
        this.crossingFactoryMap = new HashMap<>();
        this.roomFactoryMap = new HashMap<>();
        this.lootFactoryMap = new HashMap<>();
        this.lootFactoryFactoryMap = new HashMap<>();
        this.random = random;

        for (DungeonLootFactory factory : Database.dungeonLootList) {
            if (getMessage(placeKey).equals(factory.getPlace())) {
                lootFactoryFactoryMap.compute(factory.getContainer().toLowerCase(), (k, v) -> {
                    if (v == null) {
                        v = new ArrayList<>();
                    }
                    v.add(factory);
                    return v;
                });
            }
        }
    }

    protected void registerRoomObject(HashMap<Integer, WeightedFactoryList<RoomObject>> factoryMap, int weight, Supplier<RoomObject> supplier) {
        registerRoomObject(factoryMap, 1, weight, supplier);
    }

    protected void registerRoomObject(HashMap<Integer, WeightedFactoryList<RoomObject>> factoryMap, int width, int weight, Supplier<RoomObject> supplier) {
        WeightedFactoryList<RoomObject> factory = factoryMap.get(width);
        if (factory == null) {
            factory = new WeightedFactoryList<>(random);
            factoryMap.put(width, factory);
        }
        factory.add(weight, supplier);
    }

    protected void registerLootObject(LootObjectType type, int weight, String containerKey, LootObjectSupplier supplier) {
        WeightedLootObjectFactoryList factory = lootFactoryMap.get(type);
        if (factory == null) {
            factory = new WeightedLootObjectFactoryList(random);
            lootFactoryMap.put(type, factory);
        }
        factory.add(weight, getMessage(containerKey), supplier);
    }

    public TextureHandler getTextureHandler() {
        return textureHandler;
    }

    public Collection<LootFactory> getLoot(String container) {
        return lootFactoryFactoryMap.getOrDefault(container.toLowerCase(), Collections.emptyList());
    }

    public Optional<RoomObject> getPossibleEntrance(int width) {
        if (entranceFactoryMap.get(width) != null) {
            return Optional.ofNullable(entranceFactoryMap.get(width).getRandomItem());
        }
        return Optional.empty();
    }

    public Optional<RoomObject> getPossibleCorridor(int width) {
        if (corridorFactoryMap.get(width) != null) {
            return Optional.ofNullable(corridorFactoryMap.get(width).getRandomItem());
        }
        return Optional.empty();
    }

    public Optional<RoomObject> getPossibleCrossings(int width) {
        if (crossingFactoryMap.get(width) != null) {
            return Optional.ofNullable(crossingFactoryMap.get(width).getRandomItem());
        }
        return Optional.empty();
    }

    public Optional<RoomObject> getPossibleRoom(int width) {
        if (roomFactoryMap.get(width) != null) {
            return Optional.ofNullable(roomFactoryMap.get(width).getRandomItem());
        }
        return Optional.empty();
    }

    public Optional<LootObject> getLootObject(LootObjectType type, SeededRandom random, IPosition parent, int offsetX, int offsetY, int offsetZ) {
        if (lootFactoryMap.get(type) != null) {
            return Optional.ofNullable(lootFactoryMap.get(type).getRandomLootObject(random, parent, offsetX, offsetY, offsetZ));
        }
        return Optional.empty();
    }
}
