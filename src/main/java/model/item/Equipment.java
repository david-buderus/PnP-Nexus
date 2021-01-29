package model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import manager.Database;
import manager.TypTranslation;
import manager.Utility;
import model.Currency;
import model.upgrade.Upgrade;
import model.upgrade.UpgradeFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class Equipment extends Item {

    protected static Random rand = new Random();

    protected String material;
    protected int upgradeSlots;
    protected ArrayList<Upgrade> upgrades;
    protected IntegerProperty wear;
    protected IntegerProperty wearStep;
    protected Collection<Consumer<Equipment>> onBreakListeners;

    public Equipment() {
        super();
        this.material = "";
        this.upgradeSlots = 0;
        this.upgrades = new ArrayList<>();
        this.wearStep = new SimpleIntegerProperty(0);
        this.wear = new SimpleIntegerProperty(0);

        int neededSteps = Utility.getConfig().getInt("character.wear.stepsNeeded");

        if (neededSteps > 0) {
            this.wear.bind(wearStep.divide(neededSteps));
        }
        this.onBreakListeners = new ArrayList<>();
    }

    protected abstract boolean shouldBreak();

    /** Applies one wear to this equipment */
    public void applyWear() {
        applyWear(1);
    }

    /**
     * Applies wear to this equipment
     *
     * @param wear the amount of wear
     */
    public void applyWear(int wear) {
        this.setWearStep(getWearStep() + wear);
        if (shouldBreak()) {
            for (Consumer<Equipment> listener : this.onBreakListeners) {
                listener.accept(this);
            }
        }
    }

    public Equipment copy() {
        Equipment equipment = (Equipment) super.copy();
        equipment.setMaterial(this.getMaterial());
        equipment.setUpgradeSlots(this.getUpgradeSlots());

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
            if (factory.getSlots() <= equipment.getUpgradeSlots()) {
                equipment.getUpgrades().add(factory.getUpgrade());
                equipment.setUpgradeSlots(getUpgradeSlots() - factory.getSlots());
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

    public int getUpgradeSlots() {
        return upgradeSlots;
    }

    public void setUpgradeSlots(int upgradeSlots) {
        this.upgradeSlots = upgradeSlots;
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

        return this.getMaterial().equals(other.getMaterial()) && this.getUpgradeSlots() == other.getUpgradeSlots() &&
                this.upgradesAsString().equals(other.upgradesAsString());
    }
}
