package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class EnumKeyDeserializer<E extends Enum<E>> extends KeyDeserializer {

    protected Class<E> enumClass;

    public EnumKeyDeserializer(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E deserializeKey(String key, DeserializationContext ctxt) {
        return Enum.valueOf(enumClass, key);
    }
}
