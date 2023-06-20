package de.pnp.manager.model.map;

import de.pnp.manager.model.map.object.IPosition;
import de.pnp.manager.model.map.object.loot.LootObject;
import de.pnp.manager.model.map.specification.LootObjectSupplier;

import java.util.ArrayList;

public class WeightedLootObjectFactoryList {

    protected ArrayList<Entry> entries;
    protected int sumWeight;
    protected SeededRandom random;

    public WeightedLootObjectFactoryList(SeededRandom random) {
        this.entries = new ArrayList<>();
        this.sumWeight = 0;
        this.random = random;
    }

    public void add(int weight, String container, LootObjectSupplier supplier) {
        this.entries.add(new Entry(sumWeight + weight, container, supplier));
        this.sumWeight += weight;
    }

    public LootObject getRandomLootObject(SeededRandom random, IPosition parent, int offsetX, int offsetY, int offsetZ) {
        if (sumWeight > 0) {
            int num = this.random.getRandom().nextInt(sumWeight);
            for (Entry entry : entries) {
                if (num < entry.getWeight()) {
                    return entry.getSupplier().get(random, parent, offsetX, offsetY, offsetZ, entry.getContainer());
                }
            }
        }
        return null;
    }

    protected static class Entry {

        protected final int weight;
        protected final String container;
        protected final LootObjectSupplier supplier;

        public Entry(int weight, String container, LootObjectSupplier supplier) {
            this.weight = weight;
            this.container = container;
            this.supplier = supplier;
        }

        public int getWeight() {
            return weight;
        }

        public String getContainer() {
            return container;
        }

        public LootObjectSupplier getSupplier() {
            return supplier;
        }
    }
}
