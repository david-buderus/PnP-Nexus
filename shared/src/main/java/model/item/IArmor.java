package model.item;

public interface IArmor extends IEquipment {

    IArmor copy();

    int getProtection();

    void setProtection(int protection);

    int getProtectionWithWear();

    double getWeight();

    void setWeight(double weight);
}
