package de.pnp.manager.model.map.specification.texture;

import javafx.scene.image.Image;

public class CryptTexture extends TextureHandler {

    public CryptTexture() {
        this.corridor = new Image("map/crypt/room/CryptCorridor.png");
        this.turningCorridor = new Image("map/crypt/room/CryptTurningCorridor.png");
        this.corridorCrossing = new Image("map/crypt/room/CryptCorridorCrossing.png");
        this.stairs = new Image("map/crypt/room/CryptStairs.png");
        this.room = new Image("map/crypt/room/CryptRoom.png");
        this.wall = new Image("map/crypt/CryptWall.png");
        this.door = new Image("map/crypt/CryptDoor.png");
        this.chest = new Image("map/crypt/SimpleChest.png");
        this.coffin = new Image("map/crypt/SimpleCoffin.png");
    }
}
