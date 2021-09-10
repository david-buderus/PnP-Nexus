package de.pnp.manager.network.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class BaseMessage {

    public final MessageType type;
    public final Date timestamp;

    @JsonCreator
    public BaseMessage(
            @JsonProperty("type") MessageType type,
            @JsonProperty("timestamp") Date timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }
}
