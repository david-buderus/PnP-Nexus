package de.pnp.manager.network.message.login;

import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.MessageType;

import java.util.Date;

public class LoginRequestMessage extends BaseMessage<LoginRequestMessage.LoginRequestData> {

    public LoginRequestMessage() { }

    public LoginRequestMessage(String name, Date timestamp) {
        super(MessageType.loginRequest, timestamp);
        LoginRequestData data = new LoginRequestData();
        data.setName(name);
        this.setData(data);
    }

    public static class LoginRequestData {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
