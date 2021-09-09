package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.pnp.manager.model.attribute.ISecondaryAttribute;
import de.pnp.manager.model.member.generation.SecondaryAttribute;

import java.io.IOException;

public class SecondaryAttributeDeserializer extends StdDeserializer<ISecondaryAttribute> {

    public SecondaryAttributeDeserializer() {
        super(SecondaryAttribute.class);
    }

    @Override
    public ISecondaryAttribute deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return SecondaryAttribute.valueOf(p.getValueAsString());
    }
}
