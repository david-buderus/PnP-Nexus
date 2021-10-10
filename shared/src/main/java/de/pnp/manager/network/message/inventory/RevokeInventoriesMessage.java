package de.pnp.manager.network.message.inventory;

import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.REVOKE_INVENTORIES;

public class RevokeInventoriesMessage extends DataMessage<Collection<String>> {

    public RevokeInventoriesMessage() {
    }

    public RevokeInventoriesMessage(String inventoryID, Date timestamp) {
        this(Collections.singletonList(inventoryID), timestamp);
    }

    public RevokeInventoriesMessage(Collection<String> inventoryIDs, Date timestamp) {
        super(REVOKE_INVENTORIES, timestamp);
        this.setData(inventoryIDs);
    }
}
