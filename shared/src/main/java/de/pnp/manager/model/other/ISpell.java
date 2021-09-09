package de.pnp.manager.model.other;

public interface ISpell {
    int getTier();

    void setTier(int tier);

    String getName();

    void setName(String name);

    String getEffect();

    void setEffect(String effect);

    String getType();

    void setType(String type);

    String getCost();

    void setCost(String cost);

    String getCastTime();

    void setCastTime(String castTime);

    ITalent getTalent();

    void setTalent(ITalent talent);
}
