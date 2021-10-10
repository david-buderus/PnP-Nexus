package de.pnp.manager.network.message.inventory;

import de.pnp.manager.model.item.IItem;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.MOVE_ITEM_REQUEST;

public class MoveItemRequestMessage extends DataMessage<MoveItemRequestMessage.MoveItemData> {

    public MoveItemRequestMessage() {
    }

    public MoveItemRequestMessage(String from, String to, Collection<? extends IItem> items, Date timestamp) {
        super(MOVE_ITEM_REQUEST, timestamp);
        MoveItemData data = new MoveItemData();
        data.setFrom(from);
        data.setTo(to);
        data.setItems(items);
        this.setData(data);
    }

    public static class MoveItemData {
        protected String from;
        protected String to;
        protected Collection<? extends IItem> items;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public Collection<? extends IItem> getItems() {
            return items;
        }

        public void setItems(Collection<? extends IItem> items) {
            this.items = items;
        }
    }
}
