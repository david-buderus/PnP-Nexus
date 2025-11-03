package de.pnp.manager.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.pnp.manager.Tag;
import java.io.IOException;
import org.bson.types.ObjectId;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.jackson.JsonComponent;

/**
 * Definitions of how to convert {@link Tag}.
 */
@JsonComponent
public class TagConverter {

    static {
        // Define ObjectId as a String in the OpenAPI schema
        SpringDocUtils.getConfig().replaceWithClass(Tag.class, String.class);
    }

    /**
     * A custom {@link JsonSerializer} for {@link Tag}.
     */
    public static class TagJsonSerializer extends JsonSerializer<Tag> {

        @Override
        public void serialize(Tag tag, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(tag.name());
        }
    }

    /**
     * A custom {@link JsonDeserializer} for {@link ObjectId}.
     */
    public static class TagJsonDeserializer extends JsonDeserializer<Tag> {

        @Override
        public Tag deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Tag(p.getValueAsString());
        }
    }
}
