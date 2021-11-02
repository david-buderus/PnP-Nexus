package de.pnp.manager.model.battle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BattlefieldDetails {

    protected final int width;
    protected final int height;
    protected final double[] padding;
    protected final String imagePath;
    protected final TileInfo[][] tileInfos;

    @JsonCreator
    public BattlefieldDetails(
            @JsonProperty("width") int width,
            @JsonProperty("height") int height,
            @JsonProperty("padding") double[] padding,
            @JsonProperty("imagePath") String imagePath,
            @JsonProperty("tileInfos") TileInfo[][] tileInfos) {
        this.width = width;
        this.height = height;
        this.padding = padding;
        this.imagePath = imagePath;
        this.tileInfos = tileInfos;
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

    public TileInfo[][] getTileInfos() {
        return tileInfos;
    }

    public String getImagePath() {
        return imagePath;
    }

    public static class TileInfo {
        protected boolean accessible;
        protected boolean blockingVisibility;

        public TileInfo() {
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
