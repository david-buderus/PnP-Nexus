package de.pnp.manager.model.battle;

import de.pnp.manager.main.Utility;
import de.pnp.manager.model.character.PnPCharacter;
import javafx.scene.image.Image;

public class Battlefield {

    protected final Battle battle;
    protected final int width;
    protected final int height;
    protected final double[] padding;
    protected final BattlefieldTile[][] map;
    protected Image background;

    public Battlefield(Battle battle, BattlefieldDetails details) {
        this.battle = battle;
        this.width = details.getWidth();
        this.height = details.getHeight();
        this.padding = details.getPadding();
        this.map = new BattlefieldTile[width][height];
        this.background = new Image(Utility.getHomeFolder().resolve("maps").resolve(details.getImagePath()).toUri().toString());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BattlefieldTile tile = new BattlefieldTile();
                var tileInfo = details.getTileInfos()[x][y];
                tile.setAccessible(tileInfo.isAccessible());
                tile.setBlockingVisibility(tile.isBlockingVisibility());
                this.map[x][y] = tile;
            }
        }
    }

    protected void move(int fromX, int fromY, int toX, int toY) {
        if (!isOccupied(toX, toY)) {
            this.setCharacter(toX, toY, getCharacter(fromX, fromY));
            this.setCharacter(fromX, fromY, null);
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

    public PnPCharacter getCharacter(int x, int y) {
        return getTile(x, y).getCharacter();
    }

    public void setCharacter(int x, int y, PnPCharacter character) {
        getTile(x, y).setCharacter(character);
    }

    public boolean isOccupied(int x, int y) {
        return getTile(x, y).isOccupied();
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

    public Image getBackground() {
        return background;
    }

    protected static class BattlefieldTile {
        protected boolean accessible;
        protected boolean blockingVisibility;
        protected PnPCharacter character;

        protected BattlefieldTile() {
            this.accessible = true;
            this.blockingVisibility = false;
            this.character = null;
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

        public PnPCharacter getCharacter() {
            return character;
        }

        public void setCharacter(PnPCharacter character) {
            this.character = character;
        }

        public boolean isOccupied() {
            return getCharacter() != null && !getCharacter().isDead();
        }
    }
}
