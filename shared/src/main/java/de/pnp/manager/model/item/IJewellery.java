package de.pnp.manager.model.item;

import de.pnp.manager.network.serializer.DeserializerIdentifier;

public interface IJewellery extends IEquipment {

    @Override
    IJewellery copy();

    @DeserializerIdentifier
    String getGem();

    void setGem(String gem);
}
