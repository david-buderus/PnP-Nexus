package ui.map;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.map.object.IPosition;

public interface IMapCanvas {

    void drawRectangle(int x, int y, int z, int width, int depth, int rotation);

    void drawRectangle(int x, int y, int z, int width, int depth, int rotation, Color color);

    void drawImage(Image image, int x, int y, int z, int width, int depth, int rotation);

    void drawImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                   int x, int y, int z, int width, int depth, int rotation);

    void drawImage(Image image, IPosition position);

    void drawImage(Image image, IPosition position, int offsetX, int offsetY, int offsetZ, int width, int depth, int rotation);

    void drawImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                   IPosition position);

    void drawImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                   IPosition position, int offsetX, int offsetY, int offsetZ, int width, int depth, int rotation);

    void drawPerspectiveImage(Image image, int x, int y, int z, int width, int height, int depth, int rotation);

    void drawPerspectiveImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                              int x, int y, int z, int width, int height, int depth, int rotation);

    void drawPerspectiveImage(Image image, IPosition position);

    void drawPerspectiveImage(Image image, IPosition position, int offsetX, int offsetY, int offsetZ, int width, int height, int depth, int rotation);

    void drawPerspectiveImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                              IPosition position);

    void drawPerspectiveImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                              IPosition position, int offsetX, int offsetY, int offsetZ, int width, int height, int depth, int rotation);
}
