package model.map;

import model.loot.Loot;
import model.loot.LootFactory;
import model.map.object.MapObjectMap;
import model.map.object.loot.LootObject;
import model.map.object.room.Lootable;
import model.map.object.room.RoomObject;
import model.map.object.room.room.Entrance;
import model.map.object.room.room.Room;
import model.map.specification.CryptSpecification;
import model.map.specification.MapSpecification;
import ui.map.IMapCanvas;

import java.util.*;
import java.util.stream.Collectors;

public class Map {

    protected static final Random random = new Random();

    protected final MapSpecification specification;
    protected final int width, depth, height;
    protected final MapObjectMap<RoomObject> roomMap;
    protected final MapObjectMap<LootObject> lootMap;
    protected final ArrayList<RoomObject> roomObjects;
    protected final ArrayList<LootObject> lootObjects;
    protected final ArrayList<RoomObject> possibleExtensions;

    public Map(MapSpecification specification, int width, int height, int depth) {
        this.specification = specification;
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.roomMap = new MapObjectMap<>(width, height, depth);
        this.lootMap = new MapObjectMap<>(width, height, depth);
        this.roomObjects = new ArrayList<>();
        this.lootObjects = new ArrayList<>();
        this.possibleExtensions = new ArrayList<>();
    }

    public Map() {
        this(new CryptSpecification(), 50, 2, 50);
    }

    public void draw(IMapCanvas canvas) {
        for (RoomObject object : roomObjects) {
            object.draw(canvas, specification.getTextureHandler());
        }
        for (LootObject object : lootObjects) {
            object.draw(canvas, specification.getTextureHandler());
        }
    }

    public void generate() {
        generateRoomObjects();
        generateLootObjects();
        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                if (lootMap.get(x, 0, z) != null) {
                    System.out.println(x + ", " + z);
                }
            }
        }
    }

    protected void generateRoomObjects() {
        HashMap<RoomObject, Integer> failedTries = new HashMap<>();

        RoomObject entrance = new Entrance();
        if (roomMap.addMapObject(entrance, 10, 0, 10, 1)) {
            roomObjects.add(entrance);
            if (!entrance.getPossibleExtensions().isEmpty()) {
                this.possibleExtensions.add(entrance);
            }
        }

        while (!possibleExtensions.isEmpty()) {
            RoomObject object = possibleExtensions.get(random.nextInt(possibleExtensions.size()));
            possibleExtensions.remove(object);

            HashMap<Integer, RotationPoint> freeExtensions = getFreeExtensionPoints(object);
            //Check if there is a place where a new MapObject can be generated
            if (!freeExtensions.isEmpty()) {
                //Select a random exit of the extensions
                int exitId = freeExtensions.keySet().stream().skip(random.nextInt(freeExtensions.size())).findFirst().orElse(-1);
                RotationPoint exit = freeExtensions.get(exitId);
                freeExtensions.remove(exitId);

                //Get possible next MapObject
                Optional<RoomObject> optNext = object.getFollowingRoomObject(specification, object.getExitWidth(exitId));
                if (optNext.isPresent() && exit != null) {
                    RoomObject next = optNext.get();
                    //Get possible entry point of the new MapObject
                    HashMap<Integer, RotationPoint> entries = next.getRelativePossibleEntrancePoints();
                    Optional<Integer> key = entries.keySet().stream()
                            .skip(random.nextInt(entries.keySet().size())).findFirst();

                    if (key.isPresent()) {
                        RotationPoint entry = entries.get(key.get());

                        //Rotate new MapObject so it matches the rotation of the exit
                        int rotation = 0;
                        if (entry.getRotation() != exit.getRotation()) {
                            rotation = exit.getRotation() - entry.getRotation();
                            next.setRotation(rotation);
                            entry = next.getRelativePossibleEntrancePoints().get(key.get());
                        }

                        if (roomMap.addMapObject(next, exit.sub(entry).withRotation(rotation))) {
                            object.registerUsedEntry(exitId);
                            next.registerUsedEntry(key.get());
                            failedTries.put(object, 0);
                            roomObjects.add(next);
                            if (!next.getPossibleExtensions().isEmpty()) {
                                this.possibleExtensions.add(next);
                            }
                        } else {
                            failedTries.put(object, failedTries.getOrDefault(object, 0) + 1);
                        }
                    }

                    //Add it back to the list if it can still be extended and hasn't failed too often
                    if (!freeExtensions.isEmpty() && failedTries.getOrDefault(object, 0) < 5) {
                        possibleExtensions.add(object);
                    }
                }
            }
        }

        // If an open exit points randomly on an open entry, connect those
        for (RoomObject exitObject : roomObjects) {
            HashMap<Integer, RotationPoint> openExtensionPoints = exitObject.getPossibleExtensions();

            for (int exitKey : openExtensionPoints.keySet()) {
                RotationPoint exitPoint = openExtensionPoints.get(exitKey);

                if (roomMap.inBounds(exitPoint)) {
                    RoomObject entryObject = roomMap.get(exitPoint);
                    if (entryObject != null) {
                        HashMap<Integer, RotationPoint> openEntryPoints = entryObject.getPossibleEntrancePoints();

                        for (int entryKey : openEntryPoints.keySet()) {
                            RotationPoint entryPoint = openEntryPoints.get(entryKey);

                            if (exitPoint.equals(entryPoint)) {
                                exitObject.registerUsedEntry(exitKey);
                                entryObject.registerUsedEntry(entryKey);
                            }
                        }
                    }
                }

            }
        }
    }

    protected void cutDeadEnds() {
        Queue<RoomObject> queue = new LinkedList<>();
        HashMap<RoomObject, Boolean> deadEnd = new HashMap<>();
        HashMap<RoomObject, RoomObject> predecessor = new HashMap<>();

        RoomObject first = roomObjects.get(0); //RoomObjects are added in Order (maybe better solution needed)
        queue.add(first);
        deadEnd.put(first, false);

        while (!queue.isEmpty()) {
            RoomObject current = queue.poll();

            for (Point point : current.getPossibleExtensions().values()) {
                RoomObject next = roomMap.get(point);

                if(next != null) {
                    
                }
            }
        }
    }

    protected void generateLootObjects() {
        for (RoomObject roomObject : roomObjects) {
            if (roomObject instanceof Lootable) {
                for (LootObject lootObject : ((Lootable) roomObject).generateLoot()) {

                    Collection<Loot> loot = specification.getLoot(lootObject.getContainer()).stream()
                            .map(LootFactory::getLoot)
                            .filter(item -> item.getAmount() > 0)
                            .collect(Collectors.toList());

                    if (!loot.isEmpty() &&
                            lootMap.addMapObject(lootObject,
                                    roomObject.getX(), roomObject.getY(), roomObject.getZ(), roomObject.getRotation())) {
                        lootObjects.add(lootObject);
                        lootObject.setLoot(loot);
                    }
                }
            }
        }
    }

    protected HashMap<Integer, RotationPoint> getFreeExtensionPoints(RoomObject object) {
        return object.getPossibleExtensions().entrySet().stream()
                .filter(entry -> roomMap.isEmpty(entry.getValue()))
                .collect(Collectors.toMap(
                        java.util.Map.Entry::getKey,
                        java.util.Map.Entry::getValue,
                        (prev, next) -> next, HashMap::new));
    }

    public int getWidth() {
        return width;
    }

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public RoomObject getRoomObject(int x, int y, int z) {
        return roomMap.get(x, y, z);
    }

    public LootObject getLootObject(int x, int y, int z) {
        return lootMap.get(x, y, z);
    }
}
