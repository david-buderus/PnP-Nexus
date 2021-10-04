package de.pnp.manager.impl;

import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.session.Session;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.Collection;
import java.util.function.Consumer;

public class ClientImpl implements Client {

    protected String clientID;
    protected String clientName;

    @Override
    public void sendMessage(BaseMessage message) {

    }

    @Override
    public void sendActiveMessage(BaseMessage message) {

    }

    @Override
    public void setOnDisconnect(Consumer<Client> onDisconnect) {

    }

    @Override
    public ObjectProperty<Session> currentSessionProperty() {
        return null;
    }

    @Override
    public StringProperty clientNameProperty() {
        return null;
    }

    @Override
    public Collection<String> getControlledCharacters() {
        return null;
    }

    @Override
    public Collection<String> getAccessibleInventories() {
        return null;
    }

    @Override
    public boolean hasAccessToInventory(String id) {
        return false;
    }

    @Override
    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @Override
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
