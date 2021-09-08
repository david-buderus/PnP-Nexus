package model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import network.serializer.DeserializerIdentifier;

public interface IWeapon extends IEquipment {

    @Override
    IWeapon copy();

    @JsonIgnore
    boolean isShield();

    float getInitiative();

    void setInitiative(float initiative);

    String getDice();

    void setDice(String dice);

    @DeserializerIdentifier
    int getDamage();

    void setDamage(int damage);

    int getHit();

    void setHit(int hit);
}
