package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.pnp.manager.model.item.*;

import java.io.IOException;

public class IItemDeserializer extends StdDeserializer<IItem> {

    protected Class<? extends IItem> itemClass;
    protected Class<? extends IPlant> plantClass;
    protected Class<? extends IArmor> armorClass;
    protected Class<? extends IWeapon> weaponClass;
    protected Class<? extends IJewellery> jewelleryClass;

    public IItemDeserializer(
            Class<? extends IItem> itemClass,
            Class<? extends IPlant> plantClass,
            Class<? extends IArmor> armorClass,
            Class<? extends IWeapon> weaponClass,
            Class<? extends IJewellery> jewelleryClass) {
        super(IItem.class);
        this.itemClass = itemClass;
        this.plantClass = plantClass;
        this.armorClass = armorClass;
        this.weaponClass = weaponClass;
        this.jewelleryClass = jewelleryClass;
    }

    @Override
    public IItem deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode node = parser.getCodec().readTree(parser);

        if (node.get("locations") != null) {
            return mapper.treeToValue(node, plantClass);
        }
        if (node.get("weight") != null) {
            return mapper.treeToValue(node, armorClass);
        }
        if (node.get("damage") != null) {
            return mapper.treeToValue(node, weaponClass);
        }
        if (node.get("gem") != null) {
            return mapper.treeToValue(node, jewelleryClass);
        }

        return mapper.treeToValue(node, itemClass);
    }
}
