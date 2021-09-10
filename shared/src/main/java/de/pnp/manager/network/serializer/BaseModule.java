package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.pnp.manager.network.message.BaseMessage;

public class BaseModule extends SimpleModule {

    public BaseModule() {
        super();
        this.addDeserializer(BaseMessage.class, new MessageDeserializer());
    }
}
