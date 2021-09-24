package de.pnp.manager.network.message.inventory;

import de.pnp.manager.model.item.IItem;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.UPDATE_INVENTORY_NOTIFICATION;

public class InventoryUpdateNotificationMessage extends DataMessage<InventoryUpdateNotificationMessage.InventoryUpdateData> {

    public InventoryUpdateNotificationMessage() {
    }

    public InventoryUpdateNotificationMessage(
            Collection<? extends IItem> addedItems,
            Collection<? extends IItem> removedItems,
            Date timestamp) {
        super(UPDATE_INVENTORY_NOTIFICATION, timestamp);
        InventoryUpdateData data = new InventoryUpdateData();
        data.setAddedItems(addedItems);
        data.setRemovedItems(removedItems);
        this.setData(data);
    }

    public static class InventoryUpdateData {
        protected Collection<? extends IItem> addedItems;
        protected Collection<? extends IItem> removedItems;

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
