package de.pnp.manager.ui.map;

import de.pnp.manager.model.map.Map;
import de.pnp.manager.model.map.object.IPosition;
import de.pnp.manager.model.map.object.loot.LootObject;
import de.pnp.manager.model.map.object.room.RoomObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class MapCanvas extends StackPane implements IMapCanvas {

    private final Canvas mapCanvas, infoCanvas;
    private final GraphicsContext mapContext, infoContext;
    private final ObjectProperty<Map> map;
    private final IntegerProperty mouseX, mouseZ;

    private double offsetX, offsetY;
    private double prevX, prevY;
    private double zoom;

    private final IntegerProperty shownYLayer;

    public MapCanvas(ObjectProperty<Map> map, IntegerProperty shownYLayer) {
        this.mapCanvas = new Canvas(300, 300);
        this.infoCanvas = new Canvas(300, 300);
        this.mapContext = mapCanvas.getGraphicsContext2D();
        this.infoContext = infoCanvas.getGraphicsContext2D();
        this.mouseX = new SimpleIntegerProperty(0);
        this.mouseZ = new SimpleIntegerProperty(0);

        this.offsetX = 0;
        this.offsetY = 0;
        this.zoom = 10;

        this.map = map;
        this.shownYLayer = shownYLayer;
        this.shownYLayer.addListener((ob, o, n) -> refresh());

        this.getChildren().add(mapCanvas);
        this.getChildren().add(infoCanvas);

        mapCanvas.widthProperty().bind(this.widthProperty());
        mapCanvas.heightProperty().bind(this.heightProperty());
        infoCanvas.widthProperty().bind(this.widthProperty());
        infoCanvas.heightProperty().bind(this.heightProperty());

        this.setOnMousePressed(event -> {
            prevX = event.getX();
            prevY = event.getY();
        });
        this.setOnMouseDragged(event -> {
            double x = event.getX();
            double y = event.getY();
            moveOffset((prevX - x) / zoom, (prevY - y) / zoom);
            prevX = x;
            prevY = y;
        });
        this.setOnScroll(event -> {
            zoom += event.getDeltaY() / 100;
            moveOffset(event.getDeltaY() / 200, event.getDeltaY() / 200);
            refresh();
        });
        this.setOnMouseMoved(event -> {
            infoContext.save();
            infoContext.clearRect(0, 0, infoCanvas.getWidth(), infoCanvas.getHeight());
            if (map.get() != null) {
                int mapX = (int) (event.getX() / zoom + offsetX);
                int mapZ = (int) (event.getY() / zoom + offsetY);

                this.mouseX.set(mapX);
                this.mouseZ.set(mapZ);

                this.drawInfoHud(event.getX(), event.getY(), mapX, mapZ);
            }
            infoContext.restore();
        });
        this.map.addListener((ob, o, n) -> {
            this.offsetX = -(getWidth() / zoom - n.getWidth()) / 2;
            this.offsetY = -(getHeight() / zoom - n.getDepth()) / 2;
        });
    }

    public void refresh() {
        clear();
        if (map.get() != null) {
            mapContext.save();
            mapContext.setFill(Color.WHITE);
            mapContext.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

            mapContext.setFill(Color.BLACK);
            mapContext.fillRect((-offsetX - 1) * zoom, (-offsetY - 1) * zoom,
                    (map.get().getWidth() + 2) * zoom, (map.get().getDepth() + 2) * zoom);
            drawRectangle(0, shownYLayer.get(), 0, map.get().getWidth(), map.get().getDepth(), 0, Color.WHITE);

            mapContext.restore();


            map.get().draw(this);
        }
    }

    protected void drawHelp() {
        for (int z = 0; z < map.get().getDepth(); z++) {
            for (int x = 0; x < map.get().getWidth(); x++) {
                RoomObject room = map.get().getRoomObject(x, shownYLayer.get(), z);
                if (room != null) {
                    mapContext.save();
                    mapContext.setFill(Color.RED);
                    mapContext.fillRect(x * 10 + 1, z * 10 + 1, 8, 8);
                    mapContext.restore();
                }
            }
        }
    }

    public void clear() {
        infoContext.clearRect(0, 0, infoCanvas.getWidth(), infoCanvas.getHeight());
        mapContext.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
    }

    @Override
    public void drawRectangle(int x, int y, int z, int width, int depth, int rotation, Color color) {
        if (outOfBounds(x, y, z, width, 1, depth, rotation)) {
            return;
        }

        double canvasX = (x - offsetX) * zoom;
        double canvasY = (z - offsetY) * zoom;

        mapContext.save();
        mapContext.setFill(color);
        if (rotation % 2 == 0) {
            mapContext.fillRect(canvasX, canvasY, width * zoom, depth * zoom);
        } else {
            mapContext.fillRect(canvasX, canvasY, depth * zoom, width * zoom);
        }
        mapContext.restore();
    }

    @Override
    public void drawImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                          int x, int y, int z, int width, int depth, int rotation) {
        if (outOfBounds(x, y, z, width, 1, depth, rotation)) {
            return;
        }

        double canvasX = (x - offsetX) * zoom;
        double canvasY = (z - offsetY) * zoom;

        mapContext.save();
        switch (rotation) {
            case 1:
                canvasX += depth * zoom;
                break;
            case 2:
                canvasX += width * zoom;
                canvasY += depth * zoom;
                break;
            case 3:
                canvasY += width * zoom;
                break;
        }

        mapContext.transform(new Affine(new Rotate(rotation * 90, canvasX, canvasY)));
        mapContext.drawImage(image, sourceX, sourceY, sourceWidth, sourceHeight, canvasX, canvasY, width * zoom, depth * zoom);
        mapContext.restore();
    }

    @Override
    public void drawImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                          IPosition position, int offsetX, int offsetY, int offsetZ, int width, int depth, int rotation) {
        if (outOfBounds(position.getX(), position.getY(), position.getZ(), position.getWidth(), 1, position.getDepth(), position.getRotation())) {
            return;
        }

        double canvasX = (position.getX() - this.offsetX) * zoom;
        double canvasY = (position.getZ() - this.offsetY) * zoom;

        mapContext.save();
        switch (position.getRotation()) {
            case 1:
                canvasX += position.getDepth() * zoom;
                break;
            case 2:
                canvasX += position.getWidth() * zoom;
                canvasY += position.getDepth() * zoom;
                break;
            case 3:
                canvasY += position.getWidth() * zoom;
                break;
        }
        mapContext.transform(new Affine(new Rotate(position.getRotation() * 90, canvasX, canvasY)));

        canvasX += offsetX * zoom;
        canvasY += offsetZ * zoom;

        mapContext.transform(new Affine(new Rotate(rotation * 90, canvasX, canvasY)));

        switch (rotation) {
            case 1:
                canvasY -= width * zoom;
                break;
            case 2:
                canvasX -= width * zoom;
                canvasY -= width * zoom;
                break;
            case 3:
                canvasX -= width * zoom;
                break;
        }

        mapContext.drawImage(image, sourceX, sourceY, sourceWidth, sourceHeight, canvasX, canvasY, width * zoom, depth * zoom);
        mapContext.restore();
    }

    @Override
    public void drawPerspectiveImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                                     int x, int y, int z, int width, int height, int depth, int rotation) {
        if (outOfBounds(x, y, z, width, height, depth, rotation)) {
            return;
        }

        int usingHeight = shownYLayer.get() - y;

        double canvasX = (x - offsetX) * zoom;
        double canvasY = (z - offsetY) * zoom;

        sourceX += sourceWidth * ((rotation + 4) % 4);
        sourceY += sourceHeight * usingHeight;
        mapContext.drawImage(image, sourceX, sourceY, sourceWidth, sourceHeight, canvasX, canvasY, width * zoom, depth * zoom);
    }

    @Override
    public void drawPerspectiveImage(Image image, double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                                     IPosition position, int offsetX, int offsetY, int offsetZ, int width, int height, int depth, int rotation) {
        if (outOfBounds(position.getX(), position.getY() + offsetY, position.getZ(), position.getWidth(), height, position.getDepth(), position.getRotation())) {
            return;
        }

        double canvasX = (position.getX() - this.offsetX) * zoom;
        double canvasY = (position.getZ() - this.offsetY) * zoom;
        int usingHeight = shownYLayer.get() - (position.getY() + offsetY);

        mapContext.save();
        switch (position.getRotation()) {
            case 1:
                canvasX += position.getDepth() * zoom;
                break;
            case 2:
                canvasX += position.getWidth() * zoom;
                canvasY += position.getDepth() * zoom;
                break;
            case 3:
                canvasY += position.getWidth() * zoom;
                break;
        }
        mapContext.transform(new Affine(new Rotate(position.getRotation() * 90, canvasX, canvasY)));

        canvasX += offsetX * zoom;
        canvasY += offsetZ * zoom;

        mapContext.transform(new Affine(new Rotate(position.getRotation() * -90, canvasX, canvasY)));

        switch (position.getRotation()) {
            case 1:
                canvasX -= width * zoom;
                break;
            case 2:
                canvasX -= width * zoom;
                canvasY -= depth * zoom;
                break;
            case 3:
                canvasY -= width * zoom;
                break;
        }

        int size = Math.max(width, height);
        sourceX += sourceWidth * ((((rotation + position.getRotation()) % 4) + 4) % 4);
        sourceY += sourceHeight * usingHeight;
        mapContext.drawImage(image, sourceX, sourceY, sourceWidth, sourceHeight, canvasX, canvasY, size * zoom, size * zoom);
        mapContext.restore();
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffset(double offsetX, double offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        refresh();
    }

    public void moveOffset(double offsetX, double offsetY) {
        setOffset(getOffsetX() + offsetX, getOffsetY() + offsetY);
    }

    private boolean outOfBounds(int x, int y, int z, int width, int height, int depth, int rotation) {
        if (shownYLayer.get() < y || y + height <= shownYLayer.get()) {
            return true;
        }
        if (rotation % 2 == 0) {
            if (x + width < offsetX || z + depth < offsetY) {
                return true;
            }
        } else {
            if (x + depth < offsetX || z + width < offsetY) {
                return true;
            }
        }
        return mapCanvas.getWidth() / zoom + offsetX < x || mapCanvas.getHeight() / zoom + offsetY < z;
    }

    private final Label label = new Label("Test");

    private void drawInfoHud(double x, double y, int mapX, int mapZ) {
        LootObject loot = map.get().getLootObject(mapX, shownYLayer.get(), mapZ);
        if (loot != null) {
            String infoText = loot.getInfoText();
            long lines = infoText.lines().count();

            infoContext.setFill(Color.DARKGRAY);
            infoContext.fillPolygon(new double[]{x, x + 20, x + 20}, new double[]{y, y, y + 20}, 3);
            infoContext.fillRoundRect(x + 10, y, 100, 40 + 15 * lines, 20, 20);
            infoContext.setFill(Color.LIGHTGRAY);
            infoContext.fillRoundRect(x + 13, y + 3, 94, 34 + 15 * lines, 17, 17);
            infoContext.setFill(Color.DARKGRAY);
            infoContext.fillRect(x + 15, y + 24, 90, 2);
            infoContext.setFill(Color.BLACK);
            infoContext.fillText(loot.getContainer(), x + 20, y + 20, 80);
            infoContext.fillText(infoText, x + 20, y + 40, 80);
        }
    }

    public ReadOnlyIntegerProperty getMouseX() {
        return mouseX;
    }

    public ReadOnlyIntegerProperty getMouseY() {
        return shownYLayer;
    }

    public ReadOnlyIntegerProperty getMouseZ() {
        return mouseZ;
    }
}
