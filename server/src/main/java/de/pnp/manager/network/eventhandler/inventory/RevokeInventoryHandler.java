package de.pnp.manager.network.eventhandler.inventory;

import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.inventory.RevokeInventoriesMessage;
import de.pnp.manager.network.state.IEventHandler;
import de.pnp.manager.network.state.INonConditionalEventHandler;

public class RevokeInventoryHandler implements IEventHandler<BaseMessage>, INonConditionalEventHandler<BaseMessage> {

    protected Client client;

    public RevokeInventoryHandler(Client client) {
        this.client = client;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof RevokeInventoriesMessage) {
            RevokeInventoriesMessage revokeMessage = (RevokeInventoriesMessage) message;
            client.getAccessibleInventories().removeAll(revokeMessage.getData());
        }
    }
}
