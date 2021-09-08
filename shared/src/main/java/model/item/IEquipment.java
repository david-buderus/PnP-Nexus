package model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.upgrade.IUpgrade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public interface IEquipment extends IItem {

    /**
     * Applies one wear to this equipment
     */
    default void applyWear() {
        applyWear(1);
    }

    /**
     * Applies wear to this equipment
     *
     * @param wear the amount of wear
     */
    void applyWear(int wear);

    @JsonIgnore
    IEquipment getWithUpgrade();

    String getMaterial();

    void setMaterial(String material);

    int getUpgradeSlots();

    void setUpgradeSlots(int upgradeSlots);

    Collection<IUpgrade> getUpgrades();

    void setUpgrades(ArrayList<IUpgrade> upgrades);

    @JsonIgnore
    default String upgradesAsString() {
        return getUpgrades().stream().map(IUpgrade::getFullName).collect(Collectors.joining(", "));
    }

    @JsonIgnore
    int getWearStage();

    int getWearTick();

    void setWearTick(int wearTick);

    @Override
    IEquipment copy();
}
