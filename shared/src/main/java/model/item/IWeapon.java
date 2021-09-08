package model.item;

public interface IWeapon extends IEquipment {

    @Override
    IWeapon copy();

    boolean isShield();

    float getInitiative();

    void setInitiative(float initiative);

    String getDice();

    void setDice(String dice);

    int getDamage();

    void setDamage(int damage);

    int getHit();

    void setHit(int hit);
}
