package de.pnp.manager.network.message.inventory;

import de.pnp.manager.model.item.IItem;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.CREATE_ITEM_REQUEST;

public class CreateItemRequestMessage extends DataMessage<CreateItemRequestMessage.CreateItemData> {

    public CreateItemRequestMessage() {
    }

    public CreateItemRequestMessage(String inventoryID, IItem item, Date timestamp) {
        this(inventoryID, Collections.singleton(item), timestamp);
    }

    public CreateItemRequestMessage(String inventoryID, Collection<? extends IItem> items, Date timestamp) {
        super(CREATE_ITEM_REQUEST, timestamp);
        CreateItemData data = new CreateItemData();
        data.setInventoryID(inventoryID);
        data.setItems(items);
        this.setData(data);
    }

    public static class CreateItemData {

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
