package de.pnp.manager.model.battle.battlefield;

import de.pnp.manager.ui.battle.battlefield.BattlefieldCanvasCalculator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PnPCharacterObject extends RectBattlefieldObject {

    protected Image image;

    public PnPCharacterObject(double x, double y, Image image) {
        this(x, y, 1, 1, image);
    }

    public PnPCharacterObject(double x, double y, double width, double height, Image image) {
        super(x, y, width, height);
        this.image = image;
    }

    @Override
    public void draw(GraphicsContext context, BattlefieldCanvasCalculator calculator) {
        context.drawImage(image, calculator.toCanvasX(x), calculator.toCanvasY(y),
                calculator.toCanvasLength(width), calculator.toCanvasLength(height));
    }
}
