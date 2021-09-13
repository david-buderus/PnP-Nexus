package de.pnp.manager.network.message.session;

import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.JOIN_SESSION_REQUEST;

public class JoinSessionRequestMessage extends DataMessage<JoinSessionRequestMessage.JoinSessionRequestData> {

    public JoinSessionRequestMessage() {
    }

    public JoinSessionRequestMessage(String sessionId, char[] password, Date timestamp) {
        super(JOIN_SESSION_REQUEST, timestamp);
        JoinSessionRequestData data = new JoinSessionRequestData();
        data.setSessionId(sessionId);
        data.setPassword(password);
        this.setData(data);
    }

    public static class JoinSessionRequestData {
        protected String sessionId;
        protected char[] password;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public char[] getPassword() {
            return password;
        }

        public void setPassword(char[] password) {
            this.password = password;
        }
    }
}
