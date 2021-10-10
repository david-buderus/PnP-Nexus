package de.pnp.manager.network.message.inventory;

import de.pnp.manager.model.item.IItem;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.UPDATE_INVENTORY_NOTIFICATION;

public class InventoryUpdateNotificationMessage extends DataMessage<Collection<InventoryUpdateNotificationMessage.InventoryUpdateData>> {

    public InventoryUpdateNotificationMessage() {
    }

    public InventoryUpdateNotificationMessage(
            String inventoryID,
            Collection<? extends IItem> addedItems,
            Collection<? extends IItem> removedItems,
            Date timestamp) {
        this(Collections.singleton(new InventoryUpdateData(inventoryID, addedItems, removedItems)), timestamp);
    }

    public InventoryUpdateNotificationMessage(
            Collection<InventoryUpdateData> data,
            Date timestamp) {
        super(UPDATE_INVENTORY_NOTIFICATION, timestamp);
        this.setData(data);
    }

    public static class InventoryUpdateData {
        protected String inventoryID;
        protected Collection<? extends IItem> addedItems;
        protected Collection<? extends IItem> removedItems;

        public InventoryUpdateData() {
        }

        public InventoryUpdateData(String inventoryID, Collection<? extends IItem> addedItems, Collection<? extends IItem> removedItems) {
            this.inventoryID = inventoryID;
            this.addedItems = addedItems;
            this.removedItems = removedItems;
        }

        public String getInventoryID() {
            return inventoryID;
        }

        public void setInventoryID(String inventoryID) {
            this.inventoryID = inventoryID;
        }

        public Collection<? extends IItem> getAddedItems() {
            return addedItems;
        }

        public void setAddedItems(Collection<? extends IItem> addedItems) {
            this.addedItems = addedItems;
        }

        public Collection<? extends IItem> getRemovedItems() {
            return removedItems;
        }

        public void setRemovedItems(Collection<? extends IItem> removedItems) {
            this.removedItems = removedItems;
        }
    }
}
