package de.pnp.manager.app.network.serialization

import de.pnp.manager.app.model.*
import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.IRarity
import de.pnp.manager.model.character.data.IPrimaryAttribute
import de.pnp.manager.model.character.data.ISecondaryAttribute
import de.pnp.manager.model.item.*
import de.pnp.manager.model.other.ITalent
import de.pnp.manager.network.serializer.BaseModule
import de.pnp.manager.network.serializer.IEquipmentDeserializer
import de.pnp.manager.network.serializer.IItemDeserializer

class SerializationModule() : BaseModule() {

    init {
        addAbstractTypeMapping(IRarity::class.java, Rarity::class.java)
        addAbstractTypeMapping(
            IPrimaryAttribute::class.java,
            PrimaryAttribute::class.java
        )
        addAbstractTypeMapping(
            ISecondaryAttribute::class.java,
            SecondaryAttribute::class.java
        )
        addAbstractTypeMapping(ITalent::class.java, Talent::class.java)
        addAbstractTypeMapping(IArmor::class.java, Armor::class.java)
        addAbstractTypeMapping(IWeapon::class.java, Weapon::class.java)
        addAbstractTypeMapping(IJewellery::class.java, Jewellery::class.java)
        addDeserializer(
            IItem::class.java, IItemDeserializer(
                Item::class.java,
                Plant::class.java,
                Armor::class.java,
                Weapon::class.java,
                Jewellery::class.java
            )
        )
        addDeserializer(
            IEquipment::class.java, IEquipmentDeserializer(
                Armor::class.java,
                Weapon::class.java,
                Jewellery::class.java
            )
        )
        addDeserializer(ICurrency::class.java, CurrencyDeserializer())
        this.addSerializer(ICurrency::class.java, CurrencySerializer())
    }
}