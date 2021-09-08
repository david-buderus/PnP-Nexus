package model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import network.serializer.DeserializerIdentifier;

public interface IArmor extends IEquipment {

    IArmor copy();

    int getProtection();

    void setProtection(int protection);

    @JsonIgnore
    int getProtectionWithWear();

    @DeserializerIdentifier
    double getWeight();

    void setWeight(double weight);
}
