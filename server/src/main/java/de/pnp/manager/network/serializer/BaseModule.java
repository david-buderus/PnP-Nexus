package de.pnp.manager.network.serializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.IRarity;
import de.pnp.manager.model.Rarity;
import de.pnp.manager.model.attribute.IPrimaryAttribute;
import de.pnp.manager.model.attribute.ISecondaryAttribute;
import de.pnp.manager.model.item.*;
import de.pnp.manager.model.member.generation.PrimaryAttribute;
import de.pnp.manager.model.member.generation.SecondaryAttribute;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.model.other.Talent;

public class BaseModule extends SimpleModule {

    public BaseModule() {
        super();

        this.addAbstractTypeMapping(IRarity.class, Rarity.class);
        this.addAbstractTypeMapping(IPrimaryAttribute.class, PrimaryAttribute.class);
        this.addAbstractTypeMapping(ISecondaryAttribute.class, SecondaryAttribute.class);
        this.addAbstractTypeMapping(ITalent.class, Talent.class);

        this.addDeserializer(IItem.class, new IItemDeserializer(Item.class, Plant.class, Armor.class, Weapon.class, Jewellery.class));
        this.addDeserializer(ICurrency.class, new CurrencyDeserializer());
        this.addSerializer(ICurrency.class, new CurrencySerializer());
    }
}
