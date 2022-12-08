package de.pnp.manager.network.message.inventory;

import de.pnp.manager.network.message.BaseMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.ACCESSIBLE_CONTAINER_REQUEST;

public class AccessibleContainerRequestMessage extends BaseMessage {

    public AccessibleContainerRequestMessage() {
    }

    public AccessibleContainerRequestMessage(Date timestamp) {
        super(ACCESSIBLE_CONTAINER_REQUEST, timestamp);
    }
}
