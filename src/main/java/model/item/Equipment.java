package model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import manager.Database;
import manager.TypTranslation;
import model.Currency;
import model.upgrade.Upgrade;
import model.upgrade.UpgradeFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class Equipment extends Item {

    protected static Random rand = new Random();

    protected String material;
    protected int slots;
    protected ArrayList<Upgrade> upgrades;
    protected IntegerProperty wear;
    protected IntegerProperty wearStep;
    protected Collection<Consumer<Equipment>> onBreakListeners;

    public Equipment() {
        super();
        this.material = "";
        this.slots = 0;
        this.upgrades = new ArrayList<>();
        this.wearStep = new SimpleIntegerProperty(0);
        this.wear = new SimpleIntegerProperty(0);
        this.wear.bind(wearStep.divide(10));
        this.onBreakListeners = new ArrayList<>();
    }

    protected abstract boolean shouldBreak();

    public void onUse() {
        this.setWearStep(getWearStep() + 1);
        if (shouldBreak()) {
            for (Consumer<Equipment> listener : this.onBreakListeners) {
                listener.accept(this);
            }
        }
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
        Collection<String> types = TypTranslation.getAllTypes(this.getSubtype());
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

    public void addOnBreakListener(Consumer<Equipment> listener) {
        this.onBreakListeners.add(listener);
    }

    public void removeOnBreakListener(Consumer<Equipment> listener) {
        this.onBreakListeners.remove(listener);
    }

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

    @Override
    public Currency getCurrency() {
        Currency currency = super.getCurrency();

        for (Upgrade upgrade : upgrades) {
            currency = currency.add(upgrade.getCost());
        }

        return currency;
    }

    public int getWear() {
        return wear.get();
    }

    public ReadOnlyIntegerProperty wearProperty() {
        return wear;
    }

    public int getWearStep() {
        return wearStep.get();
    }

    public IntegerProperty wearStepProperty() {
        return wearStep;
    }

    public void setWearStep(int wearStep) {
        this.wearStep.set(wearStep);
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
