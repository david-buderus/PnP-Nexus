package network.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import model.ICurrency;

import java.io.IOException;

public class CurrencySerializer extends StdSerializer<ICurrency> {

    public CurrencySerializer() {
        super(ICurrency.class);
    }

    @Override
    public void serialize(ICurrency currency, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(currency.getCoinString());
    }

    @Override
    public void serializeWithType(ICurrency currency, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        serialize(currency, gen, provider);
    }
}
