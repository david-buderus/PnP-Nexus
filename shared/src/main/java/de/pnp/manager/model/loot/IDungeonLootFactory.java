package de.pnp.manager.model.loot;

public interface IDungeonLootFactory extends ILootFactory {

    void setName(String name);

    String getContainer();

    void setContainer(String container);

    String getPlace();

    void setPlace(String place);
}
