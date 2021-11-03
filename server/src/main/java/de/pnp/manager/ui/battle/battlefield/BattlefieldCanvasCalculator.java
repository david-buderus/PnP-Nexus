package de.pnp.manager.ui.battle.battlefield;

public class BattlefieldCanvasCalculator {

    protected double topSpace;
    protected double leftSpace;
    protected double factor;

    public BattlefieldCanvasCalculator(double topSpace, double leftSpace, double factor) {
        this.topSpace = topSpace;
        this.leftSpace = leftSpace;
        this.factor = factor;
    }

    public double toCanvasX(double x) {
        return toCanvasLength(leftSpace + x);
    }

    public double toCanvasY(double y) {
        return toCanvasLength(topSpace + y);
    }

    public double toCanvasLength(double length) {
        return length * factor;
    }
}
