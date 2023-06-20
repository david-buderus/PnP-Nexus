package de.pnp.manager.model.map.object;

import de.pnp.manager.model.map.Point;
import de.pnp.manager.model.map.RotationPoint;

import java.util.ArrayList;
import java.util.Collection;

public class MapObjectMap<MObj extends MapObject> {

    protected final int width, depth, height;
    protected final MObj[][][] map;
    protected final ArrayList<MObj> mapObjects;

    @SuppressWarnings("unchecked")
    public MapObjectMap(int width, int height, int depth) {
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.map = (MObj[][][]) new MapObject[width][height][depth];
        this.mapObjects = new ArrayList<>();
    }

    public boolean addMapObject(MObj object, RotationPoint point) {
        return addMapObject(object, point.getX(), point.getY(), point.getZ(), point.getRotation());
    }

    public boolean addMapObject(MObj object, int x, int y, int z, int rotation) {
        ArrayList<Point> points = getPoints(object, x, y, z, rotation);

        if (isEmpty(points)) {
            for (Point point : points) {
                map[point.getX()][point.getY()][point.getZ()] = object;
            }
            object.setRotation(rotation);
            object.setCoordinates(x, y, z);
            mapObjects.add(object);

            return true;
        } else {
            return false;
        }
    }

    public boolean deleteMapObject(Point point) {
        return deleteMapObject(point.getX(), point.getY(), point.getZ());
    }

    public boolean deleteMapObject(int x, int y, int z) {
        return deleteMapObject(get(x, y, z));
    }

    public boolean deleteMapObject(MObj obj) {
        if (mapObjects.remove(obj)) {
            ArrayList<Point> points = getPoints(obj, obj.getX(), obj.getY(), obj.getZ(), obj.getRotation());

            for (Point point : points) {
                map[point.getX()][point.getY()][point.getZ()] = null;
            }
            obj.onDelete();
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty(Point point) {
        return isEmpty(point.getX(), point.getY(), point.getZ());
    }

    protected boolean isEmpty(ArrayList<Point> points) {
        for (Point point : points) {
            if (!isEmpty(point)) {
                return false;
            }
        }

        return true;
    }

    protected ArrayList<Point> getPoints(MObj object, int x, int y, int z, int rotation) {
        ArrayList<Point> result = new ArrayList<>();

        for (MapObjectPart part : object.getParts()) {
            for (int w = 0; w < part.getWidth(); w++) {
                for (int h = 0; h < part.getHeight(); h++) {
                    for (int d = 0; d < part.getDepth(); d++) {
                        RotationPoint offset = new RotationPoint(part.getOffsetX() + w, part.getOffsetY() + h, part.getOffsetZ() + d, 0);
                        rotation = ((rotation % 4) + 4) % 4;
                        offset = offset.rotate(rotation);
                        offset = offset.correctPosition(rotation, object.getWidth(), object.getDepth());

                        result.add(offset.add(x, y, z));
                    }
                }
            }
        }

        return result;
    }

    public boolean inBounds(Point point) {
        return inBounds(point.getX(), point.getY(), point.getZ());
    }

    public boolean inBounds(int x, int y, int z) {
        return 0 <= x && 0 <= y && 0 <= z && x < width && y < height && z < depth;
    }

    public MObj get(Point point) {
        return get(point.getX(), point.getY(), point.getZ());
    }

    public MObj get(int x, int y, int z) {
        if (inBounds(x, y, z)) {
            return isEmpty(x, y, z) ? null : map[x][y][z];
        } else {
            return null;
        }
    }

    public boolean isEmpty(int x, int y, int z) {
        return inBounds(x, y, z) && map[x][y][z] == null;
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

    public Collection<MObj> getAllMapObjects() {
        return mapObjects;
    }

    public void print() {
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                System.out.print(get(x, 0, z) != null ? 1 : 0);
            }
            System.out.println();
        }
    }
}
