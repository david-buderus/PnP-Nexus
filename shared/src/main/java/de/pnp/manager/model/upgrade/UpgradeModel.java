package de.pnp.manager.model.upgrade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.item.IItem;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class UpgradeModel {
    private String name;
    private String target;
    private int slots;
    private ICurrency cost;
    private String mana;
    private String effect;
    private Collection<IItem> materialList = Collections.emptyList();
    private int level;
    private String requirement;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public ICurrency getCost() {
        return cost;
    }

    public void setCost(ICurrency cost) {
        this.cost = cost;
    }

    public String getMana() {
        return mana;
    }

    public void setMana(String mana) {
        this.mana = mana;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Collection<IItem> getMaterials() {
        return this.materialList;
    }

    public void setMaterials(Collection<IItem> materials) {
        this.materialList = materials;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    @JsonIgnore
    public String getMaterialsAsString() {
        return materialList.stream().map(item -> item.getPrettyAmount() + " " + item.getName())
                .collect(Collectors.joining("\n"));
    }
}
