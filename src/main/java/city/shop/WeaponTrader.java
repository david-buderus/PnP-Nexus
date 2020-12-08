package city.shop;

import city.Shop;
import city.Town;
import manager.Database;
import model.ItemList;
import model.item.Weapon;
import ui.shop.ShopView;
import ui.shop.WeaponTraderView;

public class WeaponTrader extends Shop {


    private ItemList weapons;

    public WeaponTrader(Town town) {
        super(town, "Waffenhändler");
        this.weapons = new ItemList();

        Database.weaponList.stream().map(Weapon::copy).filter(x -> !x.getRarity().equals("legendär")).forEach(weapon -> {
            weapon.setAmount(calculateAmount(weapon));
            weapons.add(weapon);
        });
    }

    public ItemList getWeapons() {
        return weapons;
    }

    @Override
    public ShopView getView() {
        return new WeaponTraderView(this);
    }

    @Override
    public double getSpawnChance() {
        switch (town.getTyp()) {

            case bigTown:
                return 1;
            case town:
                return 1;
            case smallTown:
                return 0.8;
            case bigVillage:
                return 0.2;
            case village:
                return 0.1;
            case smallVillage:
                return 0;
        }
        return 0;
    }
}
