package de.pnp.manager.model.map;

public class RotationPoint extends Point {

    protected final int rotation;

    public RotationPoint(int x, int y, int z, int rotation) {
        super(x, y, z);
        this.rotation = (rotation % 4 + 4) % 4;
    }

    public int getRotation() {
        return rotation;
    }

    @Override
    public RotationPoint add(Point point) {
        return add(point.getX(), point.getY(), point.getZ());
    }

    @Override
    public RotationPoint add(int x, int y, int z) {
        return new RotationPoint(this.x + x, this.y + y, this.z + z, rotation);
    }

    @Override
    public RotationPoint sub(Point point) {
        return sub(point.getX(), point.getY(), point.getZ());
    }

    @Override
    public RotationPoint sub(int x, int y, int z) {
        return new RotationPoint(this.x - x, this.y - y, this.z - z, rotation);
    }

    @Override
    public RotationPoint multiply(int factor) {
        return new RotationPoint(this.x * factor, this.y * factor, this.z * factor, rotation);
    }

    /**
     * Rotates the Point (0,0,0)
     * around the y-axis
     *
     * @param rotation between 0 and 3
     * @return the rotated point
     */
    public RotationPoint rotate(int rotation) {
        return rotate(0, 0, rotation);
    }

    public RotationPoint rotate(int centerX, int centerZ, int rotation) {
        double radians = Math.toRadians(rotation * 90);
        int newX = (int) Math.round(centerX + (x - centerX) * Math.cos(radians) - (z - centerZ) * Math.sin(radians));
        int newZ = (int) Math.round(centerZ + (x - centerX) * Math.sin(radians) + (z - centerZ) * Math.cos(radians));

        return new RotationPoint(newX, y, newZ, (((rotation + this.rotation) % 4) + 4) % 4);
    }

    public RotationPoint correctPosition(int rotation, int width, int depth) {
        return (RotationPoint) super.correctPosition(rotation, width, depth);
    }

    @Override
    public String toString() {
        return super.toString() + " Rotation: " + rotation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RotationPoint) {
            RotationPoint other = (RotationPoint) obj;
            return super.equals(other) && other.rotation == rotation;
        } else {
            return false;
        }
    }
}
