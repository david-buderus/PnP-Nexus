package de.pnp.manager.network.message.login;

import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.LOGIN_REQUEST;

public class LoginRequestMessage extends DataMessage<LoginRequestMessage.LoginRequestData> {

    public LoginRequestMessage() { }

    public LoginRequestMessage(String name, Date timestamp) {
        super(LOGIN_REQUEST, timestamp);
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
