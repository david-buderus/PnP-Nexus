package de.pnp.manager.network.message;

import java.util.Date;

public abstract class DataMessage<Data> extends BaseMessage {

    protected Data data;

    public DataMessage() {
    }

    public DataMessage(int id, Date timestamp) {
        super(id, timestamp);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
