package de.pnp.manager.model.loot;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IDungeonLootFactory extends ILootFactory {

    @JsonIgnore
    void setName(String name);

    String getContainer();

    void setContainer(String container);

    String getPlace();

    void setPlace(String place);
}
