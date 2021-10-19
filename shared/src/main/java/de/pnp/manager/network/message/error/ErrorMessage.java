package de.pnp.manager.network.message.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.ERROR;

public abstract class ErrorMessage<Data> extends DataMessage<Data> {

    public ErrorMessage() {
    }

    public ErrorMessage(int id, Date timestamp) {
        super(id, timestamp);
    }

    /**
     * Returns a human-readable error message
     * with the basic information a user could need.
     */
    @JsonIgnore
    public String getInformation() {
        return getData() != null ? getData().toString() : "";
    }
}
