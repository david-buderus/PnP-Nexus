package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.pnp.manager.model.character.Inventory;
import de.pnp.manager.model.item.IItem;

import java.io.IOException;

public class InventoryDeserializer extends StdDeserializer<Inventory> {

    protected InventoryDeserializer() {
        super(Inventory.class);
    }

    @Override
    public Inventory deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        TreeNode node = parser.getCodec().readTree(parser);

        double maxSize = mapper.treeToValue(node.get("maxSize"), Double.class);
        int maxStackSize = mapper.treeToValue(node.get("maxStackSize"), Integer.class);
        IItem[] items = mapper.treeToValue(node.get("items"), IItem[].class);


        return new Inventory(maxSize, maxStackSize, items);
    }
}
