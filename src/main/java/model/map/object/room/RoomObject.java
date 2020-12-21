package model.map.object.room;

import model.map.RotationPoint;
import model.map.SeededRandom;
import model.map.object.MapObject;
import model.map.object.MapObjectPart;
import model.map.specification.MapSpecification;
import model.map.specification.texture.TextureHandler;
import ui.map.IMapCanvas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class RoomObject extends MapObject {

    private final ArrayList<Passage> passages;

    public boolean marked = false;

    protected RoomObject(SeededRandom random, MapObjectPart... parts) {
        super(random, parts);
        this.passages = new ArrayList<>();
    }

    protected void drawWalls(IMapCanvas canvas, TextureHandler textureHandler) {
        for (Passage passage : passages) {
            if (!passage.isUsed()) {
                RotationPoint point = passage.getRelativePosition();
                canvas.drawPerspectiveImage(textureHandler.getWall(), this, point.getX(), point.getY(), point.getZ(),
                        1, 1, 1, (point.getRotation() + 2) % 4);
            }
        }
    }

    protected void addPassage(Passage passage) {
        passage.setId(passages.size());
        passages.add(passage);
    }

    public List<Passage> getPossibleExtensions() {
        return passages.stream()
                .filter(passage -> !passage.isUsed() && passage.isUsableForwards())
                .collect(Collectors.toList());
    }

    public List<Passage> getPossibleEntrancePoints() {
        return passages.stream()
                .filter(passage -> !passage.isUsed() && passage.isUsableBackwards())
                .collect(Collectors.toList());
    }

    public Optional<RoomObject> getFollowingRoomObject(MapSpecification specification, int width) {
        return Optional.empty();
    }

    public List<Passage> getAllPassages() {
        return passages;
    }

    public Passage getPassage(int id) {
        return passages.get(id);
    }

    public Collection<Passage> getPassagesTo(RoomObject roomObject) {
        return passages.stream().filter(passage -> passage.getDestination() == roomObject).collect(Collectors.toSet());
    }

    public List<RoomObject> getNeighborRooms() {
        return passages.stream()
                .filter(Passage::isUsed)
                .map(Passage::getDestination)
                .collect(Collectors.toList());
    }


    public boolean preventsDeadEnd() {
        return false;
    }

    @Override
    public void setCoordinates(int x, int y, int z) {
        super.setCoordinates(x, y, z);

        for (Passage passage : passages) {
            passage.updatePosition(rotation, getWidth(), getDepth(), getX(), getY(), getZ());
        }
    }

    @Override
    public void setRotation(int rotation) {
        super.setRotation(rotation);

        for (Passage passage : passages) {
            passage.updatePosition(rotation, getWidth(), getDepth(), getX(), getY(), getZ());
        }
    }

    @Override
    public void onDelete() {
        for (RoomObject neighbor : getNeighborRooms()) {
            for (Passage passage : neighbor.getPassagesTo(this)) {
                passage.setDestination(null);
            }
        }
    }
}
