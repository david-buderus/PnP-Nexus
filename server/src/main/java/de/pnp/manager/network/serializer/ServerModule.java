package de.pnp.manager.network.serializer;

import de.pnp.manager.model.*;
import de.pnp.manager.model.attribute.IPrimaryAttribute;
import de.pnp.manager.model.attribute.ISecondaryAttribute;
import de.pnp.manager.model.item.*;
import de.pnp.manager.model.member.generation.PrimaryAttribute;
import de.pnp.manager.model.member.generation.SecondaryAttribute;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.model.other.Talent;

public class ServerModule extends BaseModule {

    public ServerModule() {
        super();

        // Enums
        this.addAbstractTypeMapping(IRarity.class, Rarity.class);
        this.addAbstractTypeMapping(IPrimaryAttribute.class, PrimaryAttribute.class);
        this.addAbstractTypeMapping(ISecondaryAttribute.class, SecondaryAttribute.class);

        // Classes
        this.addAbstractTypeMapping(ITalent.class, Talent.class);
        this.addAbstractTypeMapping(IArmor.class, Armor.class);
        this.addAbstractTypeMapping(IWeapon.class, Weapon.class);
        this.addAbstractTypeMapping(IJewellery.class, Jewellery.class);
        this.addAbstractTypeMapping(IFabrication.class, Fabrication.class);
        this.addAbstractTypeMapping(IItemList.class, ItemList.class);

        // Inheritance
        this.addDeserializer(IItem.class, new IItemDeserializer(Item.class, Plant.class, Armor.class, Weapon.class, Jewellery.class));
        this.addDeserializer(IEquipment.class, new IEquipmentDeserializer(Armor.class, Weapon.class, Jewellery.class));

        // Special
        this.addDeserializer(ICurrency.class, new CurrencyDeserializer());
        this.addSerializer(ICurrency.class, new CurrencySerializer());
    }
}
