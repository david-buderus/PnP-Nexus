package model.map;

import java.util.ArrayList;
import java.util.function.Supplier;

public class WeightedFactoryList<Item> {

    protected ArrayList<Entry<Item>> entries;
    protected int sumWeight;
    protected SeededRandom random;

    public WeightedFactoryList(SeededRandom random) {
        this.entries = new ArrayList<>();
        this.sumWeight = 0;
        this.random = random;
    }

    public void add(int weight, Supplier<Item> supplier) {
        this.entries.add(new Entry<>(sumWeight + weight, supplier));
        this.sumWeight += weight;
    }

    public Item getRandomItem() {
        if (sumWeight > 0) {
            int num = random.getRandom().nextInt(sumWeight);
            for (Entry<Item> entry : entries) {
                if (num < entry.getWeight()) {
                    return entry.getSupplier().get();
                }
            }
        }
        return null;
    }

    protected static class Entry<Item> {

        protected int weight;
        protected Supplier<Item> supplier;

        public Entry(int weight, Supplier<Item> supplier) {
            this.weight = weight;
            this.supplier = supplier;
        }

        public int getWeight() {
            return weight;
        }

        public Supplier<Item> getSupplier() {
            return supplier;
        }
    }
}
