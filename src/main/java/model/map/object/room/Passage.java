package model.map.object.room;

import model.map.RotationPoint;

public class Passage {

    protected int id;
    protected RoomObject start, destination;
    protected boolean forwards, backwards;
    protected int width;
    protected boolean door, locked, hidden;
    protected RotationPoint relativePosition;
    protected RotationPoint relativePositionToMapObject;
    protected RotationPoint absolutePosition;

    /**
     * Represents a bidirectional passage from start room to the destination room
     *
     * @param start            start room
     * @param relativePosition offset with rotation of the room
     */
    public Passage(RoomObject start, RotationPoint relativePosition) {
        this(start, relativePosition, 1);
    }

    public Passage(RoomObject start, RotationPoint relativePosition, boolean forwards, boolean backwards) {
        this(start, relativePosition, 1, forwards, backwards);
    }

    /**
     * Represents a bidirectional passage from start room to the destination room
     *
     * @param start            start room
     * @param relativePosition offset with rotation of the room
     * @param width            width of the passage
     */
    public Passage(RoomObject start, RotationPoint relativePosition, int width) {
        this(start, relativePosition, width, true, true);
    }

    /**
     * Represents a passage from start room to the destination room which is not locked and not hidden
     *
     * @param start            start room
     * @param relativePosition offset with rotation of the room
     * @param door          the passage has a door
     */
    public Passage(RoomObject start, RotationPoint relativePosition, boolean door) {
        this(start, relativePosition, 1, true, true, door, false, false);
    }

    /**
     * Represents a passage from start room to the destination room which is not locked and not hidden
     *
     * @param start            start room
     * @param relativePosition offset with rotation of the room
     * @param width            width of the passage
     * @param door          the passage has a door
     */
    public Passage(RoomObject start, RotationPoint relativePosition, int width, boolean door) {
        this(start, relativePosition, width, true, true, door, false, false);
    }

    /**
     * Represents a passage from start room to the destination room
     *
     * @param start            start room
     * @param relativePosition offset with rotation of the room
     * @param width            width of the passage
     * @param door          the passage has a door
     * @param locked         the passage is locked
     * @param hidden         the passage is hidden
     */
    public Passage(RoomObject start, RotationPoint relativePosition, int width, boolean door, boolean locked, boolean hidden) {
        this(start, relativePosition, width, true, true, door, locked, hidden);
    }

    /**
     * Represents a passage from start room to the destination room
     *
     * @param start            start room
     * @param relativePosition offset with rotation of the room
     * @param width            width of the passage
     * @param forwards         it is possible to go from room A to room B over this passage
     * @param backwards        it is possible to go from room B to room A over this passage
     */
    public Passage(RoomObject start, RotationPoint relativePosition, int width, boolean forwards, boolean backwards) {
        this(start, relativePosition, width, forwards, backwards, false, false, false);
    }

    /**
     * Represents a passage from start room to the destination room
     *
     * @param start            start room
     * @param relativePosition offset with rotation of the room
     * @param width            width of the passage
     * @param forwards         it is possible to go from room A to room B over this passage
     * @param backwards        it is possible to go from room B to room A over this passage
     * @param door          the passage has a door
     * @param locked         the passage is locked
     * @param hidden         the passage is hidden
     */
    public Passage(RoomObject start, RotationPoint relativePosition, int width,
                   boolean forwards, boolean backwards, boolean door, boolean locked, boolean hidden) {
        this.start = start;
        this.destination = null;
        this.relativePosition = relativePosition;
        this.width = width;
        this.forwards = forwards;
        this.backwards = backwards;
        this.relativePositionToMapObject = relativePosition;
        this.absolutePosition = relativePosition;
        this.id = -1;
        this.door = door;
        this.locked = locked;
        this.hidden = hidden;
    }

    /**
     * Checks if this passage leads to another
     */
    public boolean isUsed() {
        return destination != null;
    }

    public RoomObject getStart() {
        return start;
    }

    public RoomObject getDestination() {
        return destination;
    }

    public RotationPoint getRelativePosition() {
        return relativePosition;
    }

    public RotationPoint getRelativePositionToMapObject() {
        return relativePositionToMapObject;
    }

    public RotationPoint getAbsolutePosition() {
        return absolutePosition;
    }

    public RotationPoint getAbsoluteEntryPosition() {
        RotationPoint exit = absolutePosition;
        switch (absolutePosition.getRotation() % 4) {
            case 0:
                exit = absolutePosition.add(0, 0, 1);
                break;
            case 1:
                exit = absolutePosition.sub(1, 0, 0);
                break;
            case 2:
                exit = absolutePosition.sub(0, 0, 1);
                break;
            case 3:
                exit = absolutePosition.add(1, 0, 0);
                break;
        }

        return exit.withRotation((absolutePosition.getRotation() + 2) % 4);
    }

    public int getWidth() {
        return width;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUsableForwards() {
        return forwards;
    }

    public boolean isUsableBackwards() {
        return backwards;
    }

    public boolean hasDoor() {
        return door;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setDestination(RoomObject destination) {
        this.destination = destination;
    }

    public boolean isCompatible(Passage other) {

        if (other == null) {
            return false;
        } else {
            return isUsableForwards() == other.isUsableBackwards()
                    && isUsableBackwards() == other.isUsableForwards()
                    && getWidth() == other.getWidth();
        }
    }

    public void updatePosition(int rotation, int width, int depth, int x, int y, int z) {
        this.absolutePosition =
                relativePosition.rotate(rotation)
                        .correctPosition(rotation, width, depth)
                        .add(x, y, z);
        this.relativePositionToMapObject =
                relativePosition.rotate(rotation)
                        .correctPosition(rotation, width, depth);
    }
}
