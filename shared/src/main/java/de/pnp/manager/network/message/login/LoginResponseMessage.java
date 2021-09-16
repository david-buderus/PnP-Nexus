package de.pnp.manager.network.message.login;

import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.LOGIN_RESPONSE;

public class LoginResponseMessage extends DataMessage<LoginResponseMessage.LoginResponseData> {

    public LoginResponseMessage() {
    }

    public LoginResponseMessage(String id, String name, Date timestamp) {
        super(LOGIN_RESPONSE, timestamp);
        LoginResponseData data = new LoginResponseData();
        data.setId(id);
        data.setName(name);
        this.setData(data);
    }

    public static class LoginResponseData {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
