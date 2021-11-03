package de.pnp.manager.model.battle.battlefield;

import javafx.scene.image.Image;

public class PnPCharacterObject extends BattlefieldObject {

    protected Image image;

    public PnPCharacterObject(double x, double y, Image image) {
        super(x, y);
        this.image = image;
    }

    public PnPCharacterObject(double x, double y, double width, double height, Image image) {
        super(x, y, width, height);
        this.image = image;
    }
}
