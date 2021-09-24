package de.pnp.manager.model.other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.pnp.manager.model.character.Inventory;

public class Container implements IContainer {

    protected String inventoryID;
    protected String name;
    protected Inventory inventory;

    @JsonCreator
    public Container(
            @JsonProperty("id") String inventoryID,
            @JsonProperty("name") String name,
            @JsonProperty("inventory") Inventory inventory) {
        this.inventoryID = inventoryID;
        this.name = name;
        this.inventory = inventory;
    }

    @Override
    public String getInventoryID() {
        return inventoryID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
