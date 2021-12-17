package de.pnp.manager.network.message.character.equipment;

import de.pnp.manager.model.item.IEquipment;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.UPDATE_EQUIPMENT_NOTIFICATION;

public class EquipmentUpdateNotificationMessage extends DataMessage<Collection<EquipmentUpdateNotificationMessage.EquipmentUpdateData>> {

    public EquipmentUpdateNotificationMessage() {
    }

    public EquipmentUpdateNotificationMessage(
            String characterID,
            EquipmentType type,
            Collection<? extends IEquipment> addedItems,
            Collection<? extends IEquipment> removedItems,
            Date timestamp) {
        this(Collections.singleton(new EquipmentUpdateData(characterID, type, addedItems, removedItems)), timestamp);
    }

    public EquipmentUpdateNotificationMessage(
            Collection<EquipmentUpdateData> data,
            Date timestamp) {
        super(UPDATE_EQUIPMENT_NOTIFICATION, timestamp);
        this.setData(data);
    }

    public static class EquipmentUpdateData {
        protected String characterID;
        protected EquipmentType type;
        protected Collection<? extends IEquipment> addedItems;
        protected Collection<? extends IEquipment> removedItems;

        public EquipmentUpdateData() {
        }

        public EquipmentUpdateData(String characterID, EquipmentType type, Collection<? extends IEquipment> addedItems, Collection<? extends IEquipment> removedItems) {
            this.characterID = characterID;
            this.type = type;
            this.addedItems = addedItems;
            this.removedItems = removedItems;
        }

        public String getCharacterID() {
            return characterID;
        }

        public void setCharacterID(String characterID) {
            this.characterID = characterID;
        }

        public EquipmentType getType() {
            return type;
        }

        public void setType(EquipmentType type) {
            this.type = type;
        }

        public Collection<? extends IEquipment> getAddedItems() {
            return addedItems;
        }

        public void setAddedItems(Collection<? extends IEquipment> addedItems) {
            this.addedItems = addedItems;
        }

        public Collection<? extends IEquipment> getRemovedItems() {
            return removedItems;
        }

        public void setRemovedItems(Collection<? extends IEquipment> removedItems) {
            this.removedItems = removedItems;
        }
    }
}
