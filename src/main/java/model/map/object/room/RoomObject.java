package model.map.object.room;

import model.map.Point;
import model.map.RotationPoint;
import model.map.object.MapObject;
import model.map.object.MapObjectPart;
import model.map.specification.MapSpecification;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

import java.util.*;
import java.util.stream.Collectors;

public abstract class RoomObject extends MapObject {

    private int counter;

    private final HashMap<Integer, RotationPoint> entries;
    private final HashMap<Integer, Integer> entryWidth;
    private final HashMap<Integer, RotationPoint> exits;
    private final HashMap<Integer, Integer> exitWidth;
    private final HashSet<Integer> usedEntries;

    protected RoomObject(MapObjectPart... parts) {
        super(parts);
        this.entries = new HashMap<>();
        this.exits = new HashMap<>();
        this.usedEntries = new HashSet<>();
        this.entryWidth = new HashMap<>();
        this.exitWidth = new HashMap<>();
    }

    protected void drawWalls(IMapCanvas canvas, TextureHandler textureHandler) {
        for (int key : entries.keySet()) {
            if (!usedEntries.contains(key)) {
                RotationPoint point = entries.get(key);
                canvas.drawPerspectiveImage(textureHandler.getWall(), this, point.getX(), point.getY(), point.getZ(),
                        1, 1, 1, (point.getRotation() + 2) % 4);
            }
        }
    }

    public void registerUsedEntry(int entryId) {
        this.usedEntries.add(entryId);
    }

    protected void registerEntryWithExit(RotationPoint entry) {
        this.registerEntryWithExit(entry, 1);
    }

    protected void registerEntryWithExit(RotationPoint entry, int width) {
        RotationPoint exit;
        switch (entry.getRotation()) {
            case 1:
                exit = entry.add(-1, 0, 0).withRotation(3);
                break;
            case 2:
                exit = entry.add(0, 0, -1).withRotation(0);
                break;
            case 3:
                exit = entry.add(1, 0, 0).withRotation(1);
                break;
            default:
                exit = entry.add(0, 0, 1).withRotation(2);
                break;
        }
        registerEntryWithExit(entry, exit, width);
    }

    protected void registerEntryWithExit(RotationPoint entry, RotationPoint exit, int width) {
        this.entries.put(counter, entry);
        this.exits.put(counter, exit);
        this.entryWidth.put(counter, width);
        this.exitWidth.put(counter, width);
        this.counter++;
    }

    protected void registerEntry(RotationPoint entry) {
        this.registerEntry(entry, 1);
    }

    protected void registerEntry(RotationPoint entry, int width) {
        this.entries.put(counter, entry);
        this.entryWidth.put(counter, width);
        this.counter++;
    }

    protected void registerExit(RotationPoint exit) {
        this.registerExit(exit, 1);
    }

    protected void registerExit(RotationPoint exit, int width) {
        this.exits.put(counter, exit);
        this.exitWidth.put(counter, width);
        this.counter++;
    }

    public HashMap<Integer, RotationPoint> getPossibleExtensions() {
        return exits.entrySet().stream()
                .filter(p -> !usedEntries.contains(p.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        p -> p.getValue()
                                .rotate(rotation)
                                .correctPosition(rotation, getWidth(), getDepth())
                                .add(x, y, z),
                        (prev, next) -> next, HashMap::new));
    }

    public HashMap<Integer, RotationPoint> getRelativePossibleEntrancePoints() {
        return entries.entrySet().stream()
                .filter(p -> !usedEntries.contains(p.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        p -> p.getValue()
                                .rotate(rotation)
                                .correctPosition(rotation, getWidth(), getDepth()),
                        (prev, next) -> next, HashMap::new));
    }

    public Optional<RoomObject> getFollowingRoomObject(MapSpecification specification, int width) {
        return Optional.empty();
    }

    public HashMap<Integer, RotationPoint> getPossibleEntrancePoints() {
        return entries.entrySet().stream()
                .filter(p -> !usedEntries.contains(p.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        p -> p.getValue()
                                .rotate(rotation)
                                .correctPosition(rotation, getWidth(), getDepth())
                                .add(x, y, z),
                        (prev, next) -> next, HashMap::new));
    }

    public int getEntryWidth(int key) {
        return entryWidth.getOrDefault(key, 0);
    }

    public int getExitWidth(int key) {
        return exitWidth.getOrDefault(key, 0);
    }

    public Collection<Point> getNeighborPositions() {
        return entries.entrySet().stream()
                .filter(entry -> usedEntries.contains(entry.getKey()))
                .map(Map.Entry::getValue).map(p -> p
                .rotate(rotation)
                .correctPosition(rotation, getWidth(), getDepth())
                .add(x, y, z)).collect(Collectors.toSet());
    }

    public boolean preventsDeadEnd() {
        return false;
    }
}
