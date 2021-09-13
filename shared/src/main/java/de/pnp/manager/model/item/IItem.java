package de.pnp.manager.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.IRarity;


public interface IItem {

    String getName();

    void setName(String name);

    void setType(String type);

    String getType();

    ICurrency getCurrency();

    void setCurrency(ICurrency currency);

    String getSubtype();

    void setSubtype(String subtype);

    String getRequirement();

    void setRequirement(String requirement);

    String getEffect();

    void setEffect(String effect);

    IRarity getRarity();

    void setRarity(IRarity rarity);

    int getTier();

    void setTier(int tier);

    float getAmount();

    @JsonIgnore
    default String getPrettyAmount() {
        float amount = getAmount();
        if (amount == (int) amount) {
            return Integer.toString((int) amount);
        } else {
            return Float.toString(amount);
        }
    }

    void setAmount(float amount);

    void addAmount(float amount);

    @JsonIgnore
    ICurrency getCurrencyWithAmount();

    @JsonIgnore
    boolean isTradeable();

    IItem copy();
}
