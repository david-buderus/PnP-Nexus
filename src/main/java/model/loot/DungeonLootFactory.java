package model.loot;

import manager.Utility;

public class DungeonLootFactory extends LootFactory {

    private String container;
    private String place;

    public DungeonLootFactory() {
        this("", 0, 0, "", "");
    }

    public DungeonLootFactory(String name, int maxAmount, double chance, String container, String place) {
        super(Utility.getItem(name), maxAmount, chance);
        this.container = container;
        this.place = place;
    }

    public void setName(String name) {
        this.getItem().setName(name);
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
