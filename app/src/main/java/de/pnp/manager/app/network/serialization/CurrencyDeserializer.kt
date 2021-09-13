package de.pnp.manager.app.network.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import de.pnp.manager.app.model.Currency
import de.pnp.manager.model.ICurrency

class CurrencyDeserializer : StdDeserializer<ICurrency>(ICurrency::class.java) {

    override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext
    ): Currency {
        return Currency(p.valueAsString)
    }
}