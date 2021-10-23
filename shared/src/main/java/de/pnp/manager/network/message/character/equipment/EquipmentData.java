package de.pnp.manager.network.message.character.equipment;

import de.pnp.manager.model.item.IEquipment;

public class EquipmentData {

    protected String characterID;
    protected String inventoryID;
    protected IEquipment equipment;
    protected EquipmentType type;

    public String getCharacterID() {
        return characterID;
    }

    public void setCharacterID(String characterID) {
        this.characterID = characterID;
    }

    public String getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(String inventoryID) {
        this.inventoryID = inventoryID;
    }

    public IEquipment getEquipment() {
        return equipment;
    }

    public void setEquipment(IEquipment equipment) {
        this.equipment = equipment;
    }

    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
        this.type = type;
    }
}
