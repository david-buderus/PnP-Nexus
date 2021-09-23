package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import de.pnp.manager.model.other.ITalent;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Supplier;

public class TalentKeyDeserializer extends KeyDeserializer {

    protected Supplier<Collection<? extends ITalent>> talentsGetter;

    public TalentKeyDeserializer(Supplier<Collection<? extends ITalent>> talentsGetter) {
        this.talentsGetter = talentsGetter;
    }

    @Override
    public ITalent deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        for (ITalent talent : talentsGetter.get()) {
            if (talent.getName().equalsIgnoreCase(key)) {
                return talent;
            }
        }

        throw new IOException("Unknown Talent");
    }
}
