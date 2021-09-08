package model.upgrade;

import model.ICurrency;
import model.IItemList;

import java.util.Collection;

public interface IUpgradeFactory {

    String getName();

    void setName(String name);

    String getTarget();

    void setTarget(String target);

    int getMaxLevel();

    void setMaxLevel(int maxLevel);

    int getSlots();

    void setSlots(int slots);

    void setCurrency(int level, ICurrency currency);

    ICurrency getCurrency(int level);

    ICurrency getFullCost(int level);

    void setMana(int level, String mana);

    String getMana(int level);

    void setEffect(int level, String effect);

    String getEffect(int level);

    void setMaterials(int level, IItemList materials);

    IItemList getMaterials(int level);

    IItemList getMaterialList();

    IItemList getMaterialList(int from, int to);

    IUpgrade getUpgrade();

    Collection<IUpgradeModel> getModels();

    String getRequirement(int level);

    void setRequirement(int level, String requirement);
}
