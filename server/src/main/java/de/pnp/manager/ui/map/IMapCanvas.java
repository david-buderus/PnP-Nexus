package de.pnp.manager.ui.map;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import de.pnp.manager.model.map.object.IPosition;

public interface IMapCanvas {

    default void drawRectangle(int x, int y, int z, int width, int depth, int rotation) {
        drawRectangle(x, y, z, width, depth, rotation, Color.BLACK);
    }

    void drawRectangle(int x, int y, int z, int width, int depth, int rotation, Color color);

    default void drawImage(Image image, int x, int y, int z, int width, int depth, int rotation) {
        drawImage(image, 0, 0, image.getWidth(), image.getHeight(),
                x, y, z, width, depth, rotation);
    }

    void drawImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                   int x, int y, int z, int width, int depth, int rotation);

    default void drawImage(Image image, IPosition position) {
        drawImage(image, position.getX(), position.getY(), position.getZ(), position.getWidth(), position.getDepth(), position.getRotation());
    }

    default void drawImage(Image image, IPosition position, int offsetX, int offsetY, int offsetZ, int width, int depth, int rotation) {
        drawImage(image, 0, 0, image.getWidth(), image.getHeight(), position, offsetX, offsetY, offsetZ, width, depth, rotation);
    }

    default void drawImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight, IPosition position) {
        drawImage(image, sourceX, sourceY, sourceWidth, sourceHeight, position.getX(), position.getY(), position.getZ(),
                position.getWidth(), position.getDepth(), position.getRotation());
    }

    void drawImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                   IPosition position, int offsetX, int offsetY, int offsetZ, int width, int depth, int rotation);

    default void drawPerspectiveImage(Image image, int x, int y, int z, int width, int height, int depth, int rotation) {
        drawPerspectiveImage(image, 0, 0, image.getWidth() / 4, image.getHeight() / height,
                x, y, z, width, height, depth, rotation);
    }

    void drawPerspectiveImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                              int x, int y, int z, int width, int height, int depth, int rotation);

    default void drawPerspectiveImage(Image image, IPosition position) {
        int size = Math.max(position.getWidth(), position.getDepth());
        drawPerspectiveImage(image, position.getX(), position.getY(), position.getZ(), size, position.getHeight(), size, position.getRotation());
    }

    default void drawPerspectiveImage(Image image, IPosition position, int offsetX, int offsetY, int offsetZ, int width,
                                      int height, int depth, int rotation) {
        drawPerspectiveImage(image, 0, 0, image.getWidth() / 4, image.getHeight() / height,
                position, offsetX, offsetY, offsetZ, width, height, depth, rotation);
    }

    default void drawPerspectiveImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                                      IPosition position) {
        drawPerspectiveImage(image, sourceX, sourceY, sourceWidth, sourceHeight, position.getX(), position.getY(), position.getZ(),
                position.getWidth(), position.getHeight(), position.getDepth(), position.getRotation());

    }

    void drawPerspectiveImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                              IPosition position, int offsetX, int offsetY, int offsetZ, int width, int height, int depth, int rotation);
}
