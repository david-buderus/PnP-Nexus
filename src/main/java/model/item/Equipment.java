package model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import manager.Database;
import manager.TypTranslation;
import model.Currency;
import model.upgrade.Upgrade;
import model.upgrade.UpgradeFactory;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Equipment extends Item {

    protected static Random rand = new Random();

    protected String material = "";
    protected int slots = 0;
    protected ArrayList<Upgrade> upgrades = new ArrayList<>();

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public Collection<Upgrade> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(ArrayList<Upgrade> upgrades) {
        this.upgrades = upgrades;
        this.upgrades.sort(Comparator.comparing(Upgrade::getFullName));
    }

    public String upgradesAsString() {
        return this.upgrades.stream().map(Upgrade::getFullName).collect(Collectors.joining(", "));
    }

    public Equipment copy() {
        Equipment equipment = (Equipment) super.copy();
        equipment.setMaterial(this.getMaterial());
        equipment.setSlots(this.getSlots());

        return equipment;
    }

    @JsonIgnore
    public Equipment getWithUpgrade() {

        Equipment equipment = this.copy();
        Collection<String> types = TypTranslation.getAllTypes(this.getSubTyp());
        List<UpgradeFactory> list = Database.upgradeList.stream().
                filter(x -> types.contains(x.getTarget())).collect(Collectors.toList());

        if (list.size() == 0) {
            return equipment;
        }

        while (rand.nextDouble() <= 0.2) {
            UpgradeFactory factory = list.get(rand.nextInt(list.size()));
            if (factory.getSlots() <= equipment.getSlots()) {
                equipment.getUpgrades().add(factory.getUpgrade());
                equipment.setSlots(getSlots() - factory.getSlots());
                list.remove(factory);
            }
        }

        return equipment;
    }

    @Override
    public Currency getCurrency() {
        Currency currency = super.getCurrency();

        for (Upgrade upgrade : upgrades) {
            currency = currency.add(upgrade.getCost());
        }

        return currency;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Equipment) || !super.equals(o)) {
            return false;
        }

        Equipment other = (Equipment) o;

        this.upgrades.sort(Comparator.comparing(Upgrade::getFullName));
        other.upgrades.sort(Comparator.comparing(Upgrade::getFullName));

        return this.getMaterial().equals(other.getMaterial()) && this.getSlots() == other.getSlots() &&
                this.upgradesAsString().equals(other.upgradesAsString());
    }
}
