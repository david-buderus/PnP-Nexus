package de.pnp.manager.network.message;

import java.util.Date;

public abstract class BaseMessage {

    /**
     * <pre>
     * Format: SONN
     * Example: 1203
     *
     * S := State (1 := PreLogin, 2 := Logged In, ...)
     * O := Origin (0 := Server, 1 := DM, 2 := Player/DM, 3 := everyone)
     * N := ID of the message
     * </pre>
     */
    protected int id;
    protected Date timestamp;

    protected BaseMessage() {
    }

    protected BaseMessage(int id, Date timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
