package model.item;

import model.ICurrency;
import model.IRarity;

public interface IItem {

    String getName();

    void setName(String name);

    void setType(String type);

    String getType();

    ICurrency getCurrency();

    void setCurrency(ICurrency currency);

    void setSubtype(String subtype);

    String getSubtype();

    String getRequirement();

    void setRequirement(String requirement);

    String getEffect();

    void setEffect(String effect);

    IRarity getRarity();

    void setRarity(IRarity rarity);

    int getTier();

    void setTier(int tier);

    float getAmount();

    String getPrettyAmount();

    void setAmount(float amount);

    void addAmount(float amount);

    ICurrency getCurrencyWithAmount();

    boolean isTradeable();

    IItem copy();
}
