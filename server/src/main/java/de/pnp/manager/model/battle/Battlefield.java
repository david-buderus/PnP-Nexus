package de.pnp.manager.model.battle;

import de.pnp.manager.model.battle.battlefield.BattlefieldObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Battlefield {

    protected final Battle battle;
    protected final int width;
    protected final int height;
    protected final double[] padding;
    protected final BattlefieldTile[][] map;
    protected final String imagePath;
    protected final ObservableList<BattlefieldObject> battlefieldObjects;

    public Battlefield(Battle battle, BattlefieldDetails details) {
        this.battle = battle;
        this.width = details.getWidth();
        this.height = details.getHeight();
        this.padding = details.getPadding();
        this.imagePath = details.getImagePath();
        this.map = new BattlefieldTile[width][height];
        this.battlefieldObjects = FXCollections.observableArrayList();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BattlefieldTile tile = new BattlefieldTile();
                var tileInfo = details.getTileInfos()[x][y];
                tile.setAccessible(tileInfo.isAccessible());
                tile.setBlockingVisibility(tileInfo.isBlockingVisibility());
                this.map[x][y] = tile;
            }
        }
    }

    protected BattlefieldTile getTile(int x, int y) {
        return map[x][y];
    }

    public boolean isAccessible(int x, int y) {
        return getTile(x, y).isAccessible();
    }

    public void setAccessible(int x, int y, boolean accessible) {
        getTile(x, y).setAccessible(accessible);
    }

    public boolean isBlockingVisibility(int x, int y) {
        return getTile(x, y).isBlockingVisibility();
    }

    public void setBlockingVisibility(int x, int y, boolean blockingVisibility) {
        getTile(x, y).setBlockingVisibility(blockingVisibility);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double[] getPadding() {
        return padding;
    }

    public String getImagePath() {
        return imagePath;
    }

    public ObservableList<BattlefieldObject> getBattlefieldObjects() {
        return battlefieldObjects;
    }

    protected static class BattlefieldTile {
        protected boolean accessible;
        protected boolean blockingVisibility;

        protected BattlefieldTile() {
            this.accessible = true;
            this.blockingVisibility = false;
        }

        public boolean isAccessible() {
            return accessible;
        }

        public void setAccessible(boolean accessible) {
            this.accessible = accessible;
        }

        public boolean isBlockingVisibility() {
            return blockingVisibility;
        }

        public void setBlockingVisibility(boolean blockingVisibility) {
            this.blockingVisibility = blockingVisibility;
        }
    }
}
