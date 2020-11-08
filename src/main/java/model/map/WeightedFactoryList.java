package model.map;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class WeightedFactoryList<Item> {

    protected ArrayList<Entry> entries;
    protected int sumWeight;
    protected Random random;

    public WeightedFactoryList() {
        this.entries = new ArrayList<>();
        this.sumWeight = 0;
        this.random = new Random();
    }

    public void add(int weight, Supplier<Item> supplier) {
        this.entries.add(new Entry(sumWeight + weight, supplier));
        this.sumWeight += weight;
    }

    public Item getRandomItem() {
        if(sumWeight > 0){
            int num = random.nextInt(sumWeight);
            for (Entry entry : entries) {
                if (num < entry.getWeight()) {
                    return entry.getSupplier().get();
                }
            }
        }
        return null;
    }

    protected class Entry {

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
