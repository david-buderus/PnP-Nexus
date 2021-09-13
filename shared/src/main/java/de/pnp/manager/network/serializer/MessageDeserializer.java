package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.login.LoginRequestMessage;
import de.pnp.manager.network.message.login.LoginResponseMessage;
import de.pnp.manager.network.message.session.QuerySessions;
import de.pnp.manager.network.message.session.SessionQueryResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static de.pnp.manager.network.message.MessageIDs.*;

public class MessageDeserializer extends StdDeserializer<BaseMessage> {

    protected Map<Integer, Class<? extends BaseMessage>> typeClassMap;

    public MessageDeserializer() {
        super(BaseMessage.class);
        this.typeClassMap = new HashMap<>();
        this.typeClassMap.put(LOGIN_REQUEST, LoginRequestMessage.class);
        this.typeClassMap.put(LOGIN_RESPONSE, LoginResponseMessage.class);
        this.typeClassMap.put(QUERY_SESSIONS, QuerySessions.class);
        this.typeClassMap.put(SESSION_QUERY_RESPONSE, SessionQueryResponse.class);
    }

    @Override
    public BaseMessage deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode node = parser.getCodec().readTree(parser);

        try {
            return mapper.treeToValue(node, typeClassMap.get(node.get("id").asInt()));
        } catch (IllegalArgumentException e) {
            throw new IOException("Unknown message id: " + node.get("id").asInt());
        }
    }
}
