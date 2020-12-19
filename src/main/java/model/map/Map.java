package model.map;

import model.loot.Loot;
import model.loot.LootFactory;
import model.map.object.MapObjectMap;
import model.map.object.loot.LootObject;
import model.map.object.room.Lootable;
import model.map.object.room.Passage;
import model.map.object.room.RoomObject;
import model.map.object.room.room.Entrance;
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
    protected final ArrayList<RoomObject> possibleExtensions;

    public Map(MapSpecification specification, int width, int height, int depth) {
        this.specification = specification;
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.roomMap = new MapObjectMap<>(width, height, depth);
        this.lootMap = new MapObjectMap<>(width, height, depth);
        this.possibleExtensions = new ArrayList<>();
    }

    public Map() {
        this(new CryptSpecification(), 50, 1, 50);
    }

    public void draw(IMapCanvas canvas) {
        for (RoomObject object : roomMap.getAllMapObjects()) {
            object.draw(canvas, specification.getTextureHandler());
        }
        for (LootObject object : lootMap.getAllMapObjects()) {
            object.draw(canvas, specification.getTextureHandler());
        }
    }

    public void generate() {
        generateRoomObjects();
        //cutDeadEnds();
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
        if (roomMap.addMapObject(entrance, 10, 0, 10, 0)) {
            if (!entrance.getPossibleExtensions().isEmpty()) {
                this.possibleExtensions.add(entrance);
            }
        }

        while (!possibleExtensions.isEmpty()) {
            RoomObject object = possibleExtensions.get(random.nextInt(possibleExtensions.size()));
            possibleExtensions.remove(object);

            List<Passage> freeExtensions = object.getPossibleExtensions();
            //Check if there is a place where a new MapObject can be generated
            if (!freeExtensions.isEmpty()) {
                //Select a random exit of the extensions
                Passage exit = freeExtensions.get(random.nextInt(freeExtensions.size()));
                RotationPoint exitPoint = exit.getAbsoluteEntryPosition();
                freeExtensions.remove(exit);

                //Get possible next MapObject
                Optional<RoomObject> optNext = object.getFollowingRoomObject(specification, exit.getWidth());
                if (optNext.isPresent()) {
                    RoomObject next = optNext.get();
                    //Get possible entry point of the new MapObject
                    List<Passage> entries = next.getPossibleEntrancePoints().stream()
                            .filter(p -> p.isCompatible(exit)).collect(Collectors.toList());

                    if (!entries.isEmpty()) {
                        Passage entry = entries.get(random.nextInt(entries.size()));
                        RotationPoint entryPoint = entry.getRelativePositionToMapObject();

                        //Rotate new MapObject so it matches the rotation of the exit
                        int rotation = 0;
                        if (entryPoint.getRotation() != exitPoint.getRotation()) {
                            rotation = exitPoint.getRotation() - entryPoint.getRotation();
                            next.setRotation(rotation);
                            entryPoint = entry.getRelativePositionToMapObject();
                        }

                        if (roomMap.addMapObject(next, exitPoint.sub(entryPoint).withRotation(rotation))) {
                            exit.setDestination(next);
                            entry.setDestination(next);
                            failedTries.put(object, 0);

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

    }

    protected void cutDeadEnds() {
        /*Queue<RoomObject> queue = new LinkedList<>();
        Collection<RoomObject> leaves = new ArrayList<>();
        HashMap<RoomObject, RoomObject> predecessor = new HashMap<>();
        HashMap<RoomObject, Boolean> visited = new HashMap<>();
        HashMap<RoomObject, Boolean> listed = new HashMap<>();

        RoomObject first = roomObjects.get(0); //RoomObjects are added in Order (maybe better solution needed)
        queue.add(first);
        listed.put(first, true);

        while (!queue.isEmpty()) {
            RoomObject current = queue.poll();
            visited.put(current, true);

            Collection<RoomObject> neighbors = current.getNeighborPositions().stream()
                    .map(roomMap::get)
                    .filter(Objects::nonNull)
                    .filter(room -> room != predecessor.getOrDefault(current, null))
                    .collect(Collectors.toList());

            if (neighbors.isEmpty() && !current.preventsDeadEnd()) {
                leaves.add(current);
            }

            for (RoomObject next : neighbors) {

                if (next != null && !visited.getOrDefault(next, false) && !listed.getOrDefault(next, false)) {
                    queue.add(next);
                    listed.put(next, true);
                    predecessor.put(next, current);
                }
            }
        }

        leaves.stream().map(room -> room.getX() + " " + room.getY() + " " + room.getZ()).forEach(System.out::println);

        for (RoomObject leave : leaves) {
            pruneDeadEnds(leave, predecessor);
        }*/
    }

    private void pruneDeadEnds(RoomObject deadEnd, HashMap<RoomObject, RoomObject> predecessor) {

        /*if(!deadEnd.preventsDeadEnd() && deadEnd.getNeighborPositions().size() < 3) {
            roomMap.deleteMapObject(deadEnd);
            roomObjects.remove(deadEnd);

            if(predecessor.get(deadEnd) != null) {
                pruneDeadEnds(predecessor.get(deadEnd), predecessor);
            }
        }*/
    }

    protected void generateLootObjects() {
        for (RoomObject roomObject : roomMap.getAllMapObjects()) {
            if (roomObject instanceof Lootable) {
                for (LootObject lootObject : ((Lootable) roomObject).generateLoot()) {

                    Collection<Loot> loot = specification.getLoot(lootObject.getContainer()).stream()
                            .map(LootFactory::getLoot)
                            .filter(item -> item.getAmount() > 0)
                            .collect(Collectors.toList());

                    if (!loot.isEmpty() &&
                            lootMap.addMapObject(lootObject,
                                    roomObject.getX(), roomObject.getY(), roomObject.getZ(), roomObject.getRotation())) {
                        lootObject.setLoot(loot);
                    }
                }
            }
        }
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

    public Collection<RoomObject> getRoomObjects() {
        return roomMap.getAllMapObjects();
    }
}
