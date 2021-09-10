package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.MessageType;
import de.pnp.manager.network.message.login.LoginRequestMessage;
import de.pnp.manager.network.message.login.LoginResponseMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageDeserializer extends StdDeserializer<BaseMessage<?>> {

    protected Map<MessageType, Class<? extends BaseMessage<?>>> typeClassMap;

    public MessageDeserializer() {
        super(BaseMessage.class);
        this.typeClassMap = new HashMap<>();
        this.typeClassMap.put(MessageType.loginRequest, LoginRequestMessage.class);
        this.typeClassMap.put(MessageType.loginResponse, LoginResponseMessage.class);
    }

    @Override
    public BaseMessage<?> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode node = parser.getCodec().readTree(parser);

        try {
            MessageType type = MessageType.valueOf(node.get("type").asText());
            return mapper.treeToValue(node, typeClassMap.get(type));
        } catch (IllegalArgumentException e) {
            throw new IOException("Unknown message type: " + node.get("type").asText());
        }
    }
}
