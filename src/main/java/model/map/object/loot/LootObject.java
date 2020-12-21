package model.map.object.loot;

import model.loot.Loot;
import model.map.SeededRandom;
import model.map.object.IPosition;
import model.map.object.MapObject;
import model.map.object.MapObjectPart;

import java.util.Collection;
import java.util.Collections;

public abstract class LootObject extends MapObject {

    protected final String container;
    protected final IPosition parent;
    protected Collection<Loot> loot;

    protected LootObject(SeededRandom random, String container, IPosition parent, MapObjectPart... parts) {
        super(random);
        this.container = container;
        this.parent = parent;
        this.loot = Collections.emptyList();
    }

    public String getContainer() {
        return container;
    }

    public Collection<Loot> getLoot() {
        return loot;
    }

    public void setLoot(Collection<Loot> loot) {
        this.loot = loot;
    }

    @Override
    public int getWidth() {
        return parent.getWidth();
    }

    @Override
    public int getHeight() {
        return parent.getHeight();
    }

    @Override
    public int getDepth() {
        return parent.getDepth();
    }

    @Override
    public String getInfoText() {
        int limit = 4;

        return loot.stream()
                .limit(loot.size() == limit ? limit : limit - 1)
                .map(l -> l.getAmount() + " " + l.getName())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("")
                + (loot.size() > limit ? "\n..." : "");
    }
}
