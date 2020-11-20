package city;

import model.item.Item;
import ui.shop.ShopView;

import java.util.Random;

public abstract class Shop {

    protected static final Random rand = new Random();

    protected Town town;
    protected String name;

    protected Shop(Town town, String name) {
        this.town = town;
        this.name = name;
    }

    protected int calculateAmount(Item item) {
        switch (item.getRarity()) {
            case "gewöhnlich":
                return calculateAmountSub(item.getAmount(), item.getAmount() * 9);
            case "selten":
                return calculateAmountSub((double) item.getAmount() / 2, item.getAmount() * 4.5);
            case "episch":
                return calculateAmountSub((double) item.getAmount() / 4, item.getAmount() * 2.75);
            case "legendär":
                return calculateAmountSub(0, item.getAmount() * 2);
        }

        return 0;
    }

    private int calculateAmountSub(double minimum, double random) {
        return (int) Math.round((minimum + rand.nextInt((int) Math.round(random))) * town.itemMultiplication());
    }

    public String getName() {
        return this.name;
    }

    public abstract ShopView getView();

    public abstract double getSpawnChance();
}
