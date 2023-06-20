package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.pnp.manager.model.Currency;

import java.io.IOException;

public class CurrencyDeserializer extends StdDeserializer<Currency> {

    public CurrencyDeserializer() {
        super(Currency.class);
    }

    @Override
    public Currency deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new Currency(p.getValueAsString());
    }
}
