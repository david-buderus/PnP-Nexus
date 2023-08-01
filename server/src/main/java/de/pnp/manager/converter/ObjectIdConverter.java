package de.pnp.manager.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.bson.types.ObjectId;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.jackson.JsonComponent;

/**
 * Definitions of how to convert {@link ObjectId}.
 */
@JsonComponent
public class ObjectIdConverter {

    static {
        // Define ObjectId as a String in the OpenAPI schema
        SpringDocUtils.getConfig().replaceWithClass(ObjectId.class, String.class);
    }

    /**
     * A custom {@link JsonSerializer} for {@link ObjectId}.
     */
    public static class ObjectIdJsonSerializer extends JsonSerializer<ObjectId> {

        @Override
        public void serialize(ObjectId id, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(id.toHexString());
        }
    }

    /**
     * A custom {@link JsonDeserializer} for {@link ObjectId}.
     */
    public static class ObjectIdJsonDeserializer extends JsonDeserializer<ObjectId> {
        @Override
        public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new ObjectId(p.getValueAsString());
        }
    }
}
