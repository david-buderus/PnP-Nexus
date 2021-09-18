package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.pnp.manager.model.character.IPlayerCharacter;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.item.*;

import java.io.IOException;
import java.io.InvalidClassException;

public class IPnPCharacterDeserializer extends StdDeserializer<IPnPCharacter> {

    protected Class<? extends IPnPCharacter> characterClass;
    protected Class<? extends IPlayerCharacter> playerClass;

    public IPnPCharacterDeserializer(Class<? extends IPnPCharacter> characterClass, Class<? extends IPlayerCharacter> playerClass) {
        super(IPnPCharacter.class);
        this.characterClass = characterClass;
        this.playerClass = playerClass;
    }

    @Override
    public IPnPCharacter deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode node = parser.getCodec().readTree(parser);

        if (node.get("history") != null) {
            return mapper.treeToValue(node, playerClass);
        }
        return mapper.treeToValue(node, characterClass);
    }
}