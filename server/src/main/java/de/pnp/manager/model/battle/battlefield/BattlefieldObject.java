package de.pnp.manager.model.battle.battlefield;

import javafx.scene.canvas.GraphicsContext;

public class BattlefieldObject {

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public BattlefieldObject(double x, double y) {
        this(x, y, 1, 1);
    }

    public BattlefieldObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(GraphicsContext context, double canvasX, double canvasY, double canvasWidth, double canvasHeight) {

    }

    public void moveTo(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
