package de.pnp.manager.network.message.inventory;

import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.DISMISS_CHARACTERS;
import static de.pnp.manager.network.message.MessageIDs.DISMISS_INVENTORIES;

public class DismissInventoriesMessage extends DataMessage<Collection<String>> {

    public DismissInventoriesMessage() {
    }

    public DismissInventoriesMessage(Collection<String> inventoryIDs, Date timestamp) {
        super(DISMISS_INVENTORIES, timestamp);
        this.setData(inventoryIDs);
    }
}
