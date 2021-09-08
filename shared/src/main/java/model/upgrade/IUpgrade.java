package model.upgrade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.ICurrency;

public interface IUpgrade {

    String getName();

    void setName(String name);

    String getTarget();

    void setTarget(String target);

    int getLevel();

    void setLevel(int level);

    int getSlots();

    void setSlots(int slots);

    String getEffect();

    void setEffect(String effect);

    ICurrency getCost();

    void setCost(ICurrency cost);

    @JsonIgnore
    String getFullName();
}
