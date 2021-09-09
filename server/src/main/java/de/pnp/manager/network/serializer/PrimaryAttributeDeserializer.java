package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.pnp.manager.model.attribute.IPrimaryAttribute;
import de.pnp.manager.model.member.generation.PrimaryAttribute;

import java.io.IOException;

public class PrimaryAttributeDeserializer extends StdDeserializer<IPrimaryAttribute> {

    public PrimaryAttributeDeserializer() {
        super(PrimaryAttribute.class);
    }

    @Override
    public IPrimaryAttribute deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return PrimaryAttribute.valueOf(p.getValueAsString());
    }
}
