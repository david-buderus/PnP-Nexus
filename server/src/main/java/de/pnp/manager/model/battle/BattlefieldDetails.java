package de.pnp.manager.model.battle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BattlefieldDetails {

    protected final int width;
    protected final int height;
    protected final String imagePath;
    protected final TileInfo[][] tileInfos;

    @JsonCreator
    public BattlefieldDetails(
            @JsonProperty("width") int width,
            @JsonProperty("height") int height,
            @JsonProperty("imagePath") String imagePath,
            @JsonProperty("tileInfos") TileInfo[][] tileInfos) {
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
        this.tileInfos = tileInfos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TileInfo[][] getTileInfos() {
        return tileInfos;
    }

    public String getImagePath() {
        return imagePath;
    }

    protected static class TileInfo {
        protected boolean accessible;
        protected boolean blockingVisibility;

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
