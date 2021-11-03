package de.pnp.manager.model.battle.battlefield;

import de.pnp.manager.ui.battle.battlefield.BattlefieldCanvasCalculator;
import javafx.scene.canvas.GraphicsContext;

public abstract class BattlefieldObject {

    protected double x;
    protected double y;

    public BattlefieldObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(GraphicsContext context, BattlefieldCanvasCalculator calculator);

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
}
