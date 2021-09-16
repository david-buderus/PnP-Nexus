package de.pnp.manager.network.serializer;

import de.pnp.manager.model.*;
import de.pnp.manager.model.item.*;
import de.pnp.manager.model.loot.*;
import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.Inventory;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.data.*;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.model.other.Talent;

public class ServerModule extends BaseModule {

    public ServerModule() {
        super();

        // Enums
        this.addAbstractTypeMapping(IRarity.class, Rarity.class);
        this.addAbstractTypeMapping(IArmorPiece.class, ArmorPiece.class);
        this.addAbstractTypeMapping(IArmorPosition.class, ArmorPosition.class);
        this.addAbstractTypeMapping(IAttackTypes.class, AttackTypes.class);
        this.addAbstractTypeMapping(IPrimaryAttribute.class, PrimaryAttribute.class);
        this.addAbstractTypeMapping(ISecondaryAttribute.class, SecondaryAttribute.class);

        // Classes
        this.addAbstractTypeMapping(ITalent.class, Talent.class);
        this.addAbstractTypeMapping(IArmor.class, Armor.class);
        this.addAbstractTypeMapping(IWeapon.class, Weapon.class);
        this.addAbstractTypeMapping(IJewellery.class, Jewellery.class);
        this.addAbstractTypeMapping(IFabrication.class, Fabrication.class);
        this.addAbstractTypeMapping(IItemList.class, ItemList.class);
        this.addAbstractTypeMapping(ILootTable.class, LootTable.class);
        this.addAbstractTypeMapping(ILoot.class, Loot.class);
        this.addAbstractTypeMapping(ILootFactory.class, LootFactory.class);
        this.addAbstractTypeMapping(IDungeonLootFactory.class, DungeonLootFactory.class);
        this.addAbstractTypeMapping(IInventory.class, Inventory.class);
        this.addAbstractTypeMapping(IPnPCharacter.class, PnPCharacter.class);

        // Inheritance
        this.addDeserializer(IItem.class, new IItemDeserializer(Item.class, Plant.class, Armor.class, Weapon.class, Jewellery.class));
        this.addDeserializer(IEquipment.class, new IEquipmentDeserializer(Armor.class, Weapon.class, Jewellery.class));

        // Special
        this.addDeserializer(ICurrency.class, new CurrencyDeserializer());
        this.addSerializer(ICurrency.class, new CurrencySerializer());
    }
}
