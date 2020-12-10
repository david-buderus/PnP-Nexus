package model.map.object.room;

public class Passage {

    protected RoomObject start, destination;
    protected boolean forwards, backwards;
    protected int width;

    /**
     * Represents a bidirectional passage from start room to the destination room
     *
     * @param start room A
     */
    public Passage(RoomObject start) {
        this(start, 1, true, true);
    }

    /**
     * Represents a bidirectional passage from start room to the destination room
     *
     * @param start room A
     * @param width width of the passage
     */
    public Passage(RoomObject start, int width) {
        this(start, width, true, true);
    }

    /**
     * Represents a passage from room A to room B
     *
     * @param start room A
     * @param width width of the passage
     * @param forwards  it is possible to go from start room to the destination room
     * @param backwards  it is possible to go from start room to the destination room
     */
    public Passage(RoomObject start, int width, boolean forwards, boolean backwards) {
        this(start, null, width, true, true);
    }

    /**
     * Represents a passage from room A to room B
     *
     * @param start room A
     * @param destination room b
     * @param width width of the passage
     * @param forwards  it is possible to go from room A to room B over this passage
     * @param backwards  it is possible to go from room B to room A over this passage
     */
    protected Passage(RoomObject start, RoomObject destination, int width, boolean forwards, boolean backwards) {
        this.start = start;
        this.destination = destination;
        this.width = width;
        this.forwards = forwards;
        this.backwards = backwards;
    }

    /** Checks if this passage leads to another  */
    public boolean isUsed() {
        return destination != null;
    }

    public RoomObject getStart() {
        return start;
    }

    public RoomObject getDestination() {
        return destination;
    }

    public int getWidth() {
        return width;
    }

    public boolean isUsableForwards() {
        return forwards;
    }

    public boolean isUsableBackwards() {
        return backwards;
    }

    public void setDestination(RoomObject destination) {
        this.destination = destination;
    }
    
    public boolean isCompatible(Passage other) {

        if (other == null) {
            return false;
        }
        else {
            return isUsableForwards() == other.isUsableBackwards()
                    && isUsableBackwards() == other.isUsableForwards()
                    && getWidth() == other.getWidth();
        }
    }
}
