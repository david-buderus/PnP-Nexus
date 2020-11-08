package model.map;

public class Point {

    protected final int x, y, z;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Point add(Point point) {
        return add(point.getX(), point.getY(), point.getZ());
    }

    public Point add(int x, int y, int z) {
        return new Point(this.x + x, this.y + y, this.z + z);
    }

    public Point sub(Point point) {
        return sub(point.getX(), point.getY(), point.getZ());
    }

    public Point sub(int x, int y, int z) {
        return new Point(this.x - x, this.y - y, this.z - z);
    }

    public Point multiply(int factor) {
        return new Point(factor * this.x, factor * this.y, factor * this.z);
    }

    public RotationPoint withRotation(int rotation) {
        return new RotationPoint(this.x, this.y, this.z, rotation);
    }

    public Point correctPosition(int rotation, int width, int depth) {
        switch (rotation) {
            case 1:
                return this.add(depth - 1, 0, 0);
            case 2:
                return this.add(width - 1, 0, depth - 1);
            case 3:
                return this.add(0, 0, width - 1);
        }
        return this;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Point) {
            Point other = (Point) obj;
            return x == other.x && y == other.y && z == other.z;
        } else {
            return false;
        }
    }
}
