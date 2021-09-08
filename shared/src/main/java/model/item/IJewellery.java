package model.item;

import network.serializer.DeserializerIdentifier;

public interface IJewellery extends IEquipment {

    @Override
    IJewellery copy();

    @DeserializerIdentifier
    String getGem();

    void setGem(String gem);
}
