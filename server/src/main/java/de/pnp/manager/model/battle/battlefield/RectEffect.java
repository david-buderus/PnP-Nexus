package de.pnp.manager.model.battle.battlefield;

import de.pnp.manager.ui.battle.battlefield.BattlefieldCanvasCalculator;
import javafx.scene.canvas.GraphicsContext;

public class RectEffect extends RectBattlefieldObject {

    public RectEffect(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(GraphicsContext context, BattlefieldCanvasCalculator calculator) {
        context.save();
        context.setGlobalAlpha(0.5);
        context.fillRect(calculator.toCanvasX(x), calculator.toCanvasY(y),
                calculator.toCanvasLength(width), calculator.toCanvasLength(height));
        context.restore();
    }
}
