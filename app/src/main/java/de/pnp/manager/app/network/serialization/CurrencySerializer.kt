package de.pnp.manager.app.network.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import de.pnp.manager.model.ICurrency

class CurrencySerializer : StdSerializer<ICurrency>(ICurrency::class.java) {
    override fun serialize(currency: ICurrency, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(currency.coinString)
    }

    override fun serializeWithType(
        currency: ICurrency,
        gen: JsonGenerator,
        provider: SerializerProvider,
        typeSer: TypeSerializer
    ) {
        serialize(currency, gen, provider)
    }

}