package model.map.specification;

import manager.Database;
import model.loot.DungeonLootFactory;
import model.loot.LootFactory;
import model.map.WeightedFactoryList;
import model.map.object.room.RoomObject;
import model.map.specification.texture.TextureHandler;

import java.util.*;
import java.util.function.Supplier;

public abstract class MapSpecification {

    protected final Random random = new Random();

    protected TextureHandler textureHandler;
    protected HashMap<Integer, WeightedFactoryList<RoomObject>> entranceFactoryMap;
    protected HashMap<Integer, WeightedFactoryList<RoomObject>> corridorFactoryMap;
    protected HashMap<Integer, WeightedFactoryList<RoomObject>> crossingFactoryMap;
    protected HashMap<Integer, WeightedFactoryList<RoomObject>> roomFactoryMap;

    protected HashMap<String, Collection<LootFactory>> lootFactoryMap;

    protected MapSpecification(TextureHandler textureHandler, String place) {
        this.textureHandler = textureHandler;
        this.entranceFactoryMap = new HashMap<>();
        this.corridorFactoryMap = new HashMap<>();
        this.crossingFactoryMap = new HashMap<>();
        this.roomFactoryMap = new HashMap<>();
        this.lootFactoryMap = new HashMap<>();

        for (DungeonLootFactory factory : Database.dungeonLootList) {
            if (place.equals(factory.getPlace())) {
                lootFactoryMap.compute(factory.getContainer(), (k, v) -> {
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
            factory = new WeightedFactoryList<>();
            factoryMap.put(width, factory);
        }
        factory.add(weight, supplier);
    }

    public TextureHandler getTextureHandler() {
        return textureHandler;
    }

    public Collection<LootFactory> getLoot(String container) {
        return lootFactoryMap.getOrDefault(container, Collections.emptyList());
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
}
