package de.pnp.manager.model.battle.battlefield;

public abstract class RectBattlefieldObject extends BattlefieldObject {

    protected double width;
    protected double height;

    public RectBattlefieldObject(double x, double y) {
        this(x, y, 1, 1);
    }

    public RectBattlefieldObject(double x, double y, double width, double height) {
        super(x, y);
        this.height = height;
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
