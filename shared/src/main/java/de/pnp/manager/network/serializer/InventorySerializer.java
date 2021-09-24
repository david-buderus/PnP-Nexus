package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.pnp.manager.model.character.Inventory;

import java.io.IOException;

public class InventorySerializer extends StdSerializer<Inventory> {

    protected InventorySerializer() {
        super(Inventory.class);
    }

    @Override
    public void serialize(Inventory inventory, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("maxSize", inventory.getMaxSize());
        gen.writeNumberField("numberOfSlots", inventory.getNumberOfSlots());
        gen.writeObjectField("items", inventory.toArray());
        gen.writeEndObject();
    }
}
