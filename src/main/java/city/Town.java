package city;

import city.shop.Enchanter;
import city.shop.WeaponTrader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Town {

    private static Random rand = new Random();

    private String name;
    private int population;
    private TownTyp typ;

    private ArrayList<Shop> shops;
    private ArrayList<Shop> blackMarket;

    public Town(String name, int population) {
        this(name, TownTyp.getTownTyp(population), population);
    }

    public Town(String name, TownTyp typ) {
        this(name, typ, typ.getAverage() / 2 + rand.nextInt(typ.getAverage()));
    }

    private Town(String name, TownTyp typ, int population) {
        this.name = name;
        this.population = population;
        this.typ = typ;
        this.shops = new ArrayList<>();
        this.blackMarket = new ArrayList<>();

        spawnShop(new Enchanter(this));
        spawnShop(new WeaponTrader(this));
    }

    private void spawnShop(Shop shop) {
        if (rand.nextDouble() < shop.getSpawnChance()) {
            this.addShop(shop);
        }
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
        this.typ = TownTyp.getTownTyp(population);
    }

    public TownTyp getTyp() {
        return typ;
    }

    public String getName() {
        return name;
    }

    public Collection<Shop> getShops() {
        return this.shops;
    }

    public void addShop(Shop shop) {
        this.shops.add(shop);
    }

    public void removeShop(Shop shop) {
        this.shops.remove(shop);
    }

    public Collection<Shop> getBlackMarket() {
        return this.blackMarket;
    }

    public void addBlackMarketShop(Shop shop) {
        this.blackMarket.add(shop);
    }

    public void removeBlackMarketShop(Shop shop) {
        this.blackMarket.remove(shop);
    }

    public boolean hasBlackMarket() {
        return blackMarket.size() > 0;
    }

    public double itemMultiplication() {
        return (double) population / TownTyp.town.getAverage();
    }
}
