package de.pnp.manager.app.network.serialization

import de.pnp.manager.app.model.*
import de.pnp.manager.app.state.ApplicationState
import de.pnp.manager.model.*
import de.pnp.manager.model.character.IInventory
import de.pnp.manager.model.character.IPlayerCharacter
import de.pnp.manager.model.character.IPnPCharacter
import de.pnp.manager.model.character.data.*
import de.pnp.manager.model.item.*
import de.pnp.manager.model.loot.IDungeonLootFactory
import de.pnp.manager.model.loot.ILoot
import de.pnp.manager.model.loot.ILootFactory
import de.pnp.manager.model.loot.ILootTable
import de.pnp.manager.model.other.ISpell
import de.pnp.manager.model.other.ITalent
import de.pnp.manager.network.client.IClient
import de.pnp.manager.network.serializer.*
import de.pnp.manager.network.session.ISession

class SerializationModule() : BaseModule() {

    init {
        // Enums
        addAbstractTypeMapping(IRarity::class.java, Rarity::class.java)
        addAbstractTypeMapping(
            IPrimaryAttribute::class.java,
            PrimaryAttribute::class.java
        )
        addAbstractTypeMapping(
            ISecondaryAttribute::class.java,
            SecondaryAttribute::class.java
        )
        addAbstractTypeMapping(
            IArmorPiece::class.java,
            ArmorPiece::class.java
        )
        addKeyDeserializer(IArmorPosition::class.java, EnumKeyDeserializer(ArmorPosition::class.java))
        addKeyDeserializer(ITalent::class.java, TalentKeyDeserializer{ApplicationState.databaseData?.talents})
        addKeyDeserializer(IPrimaryAttribute::class.java, EnumKeyDeserializer(PrimaryAttribute::class.java))
        addKeyDeserializer(ISecondaryAttribute::class.java, EnumKeyDeserializer(SecondaryAttribute::class.java))
        addKeyDeserializer(IAttackTypes::class.java, EnumKeyDeserializer(AttackTypes::class.java))
        addKeyDeserializer(IArmorPiece::class.java, EnumKeyDeserializer(ArmorPiece::class.java))
        addKeyDeserializer(IRarity::class.java, EnumKeyDeserializer(Rarity::class.java))

        addAbstractTypeMapping(IArmorPosition::class.java, ArmorPosition::class.java)
        addAbstractTypeMapping(IAttackTypes::class.java, AttackTypes::class.java)

        // Classes
        addAbstractTypeMapping(ITalent::class.java, Talent::class.java)
        addAbstractTypeMapping(IArmor::class.java, Armor::class.java)
        addAbstractTypeMapping(IWeapon::class.java, Weapon::class.java)
        addAbstractTypeMapping(IJewellery::class.java, Jewellery::class.java)
        addAbstractTypeMapping(IFabrication::class.java, Fabrication::class.java)
        addAbstractTypeMapping(IItemList::class.java, ItemList::class.java)
        addAbstractTypeMapping(ILootTable::class.java, LootTable::class.java)
        addAbstractTypeMapping(ILoot::class.java, Loot::class.java)
        addAbstractTypeMapping(ILootFactory::class.java, LootFactory::class.java)
        addAbstractTypeMapping(IDungeonLootFactory::class.java, DungeonLootFactory::class.java)
        addAbstractTypeMapping(IInventory::class.java, Inventory::class.java)
        addAbstractTypeMapping(IPnPCharacter::class.java, PnPCharacter::class.java)
        addAbstractTypeMapping(IPlayerCharacter::class.java, PlayerCharacter::class.java)
        addAbstractTypeMapping(ISession::class.java, Session::class.java)
        addAbstractTypeMapping(IClient::class.java, Client::class.java)
        addAbstractTypeMapping(ISpell::class.java, Spell::class.java)

        // inheritance
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
        addDeserializer(
            IPnPCharacter::class.java, IPnPCharacterDeserializer(
                PnPCharacter::class.java,
                PlayerCharacter::class.java
            )
        )


        // special
        addDeserializer(ICurrency::class.java, CurrencyDeserializer())
        this.addSerializer(ICurrency::class.java, CurrencySerializer())
    }
}