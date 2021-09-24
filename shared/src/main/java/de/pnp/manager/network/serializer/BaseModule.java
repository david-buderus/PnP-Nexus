package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.pnp.manager.model.IItemList;
import de.pnp.manager.model.ItemList;
import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.character.Inventory;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.model.other.IContainer;
import de.pnp.manager.network.message.BaseMessage;

public class BaseModule extends SimpleModule {

    public BaseModule() {
        super();
        this.addAbstractTypeMapping(IContainer.class, Container.class);
        this.addAbstractTypeMapping(IItemList.class, ItemList.class);
        this.addAbstractTypeMapping(IInventory.class, Inventory.class);
        this.addDeserializer(BaseMessage.class, new MessageDeserializer());
        this.addSerializer(Inventory.class, new InventorySerializer());
        this.addDeserializer(Inventory.class, new InventoryDeserializer());
    }
}
