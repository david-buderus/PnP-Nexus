package de.pnp.manager.network.message;

import java.util.Date;

public abstract class BaseMessage {

    protected MessageType type;
    protected Date timestamp;

    protected BaseMessage() { }

    protected BaseMessage(MessageType type, Date timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
