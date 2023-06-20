package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.pnp.manager.model.IRarity;
import de.pnp.manager.model.Rarity;

import java.io.IOException;

public class RarityDeserializer extends StdDeserializer<IRarity> {

    public RarityDeserializer() {
        super(Rarity.class);
    }

    @Override
    public IRarity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return Rarity.valueOf(p.getValueAsString());
    }
}
