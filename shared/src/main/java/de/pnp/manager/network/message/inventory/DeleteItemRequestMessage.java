package de.pnp.manager.network.message.inventory;

import de.pnp.manager.model.item.IItem;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.DELETE_ITEM_REQUEST;

public class DeleteItemRequestMessage extends DataMessage<DeleteItemRequestMessage.DeleteItemData> {

    public DeleteItemRequestMessage() {
    }

    public DeleteItemRequestMessage(String inventoryID, IItem item, Date timestamp) {
        this(inventoryID, Collections.singleton(item), timestamp);
    }

    public DeleteItemRequestMessage(String inventoryID, Collection<? extends IItem> items, Date timestamp) {
        super(DELETE_ITEM_REQUEST, timestamp);
        DeleteItemRequestMessage.DeleteItemData data = new DeleteItemRequestMessage.DeleteItemData();
        data.setInventoryID(inventoryID);
        data.setItems(items);
        this.setData(data);
    }

    public static class DeleteItemData {

        protected String inventoryID;
        protected Collection<? extends IItem> items;

        public String getInventoryID() {
            return inventoryID;
        }

        public void setInventoryID(String inventoryID) {
            this.inventoryID = inventoryID;
        }

        public Collection<? extends IItem> getItems() {
            return items;
        }

        public void setItems(Collection<? extends IItem> items) {
            this.items = items;
        }
    }
}
