package network.serializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import model.ICurrency;
import model.IRarity;
import model.Rarity;
import model.attribute.IPrimaryAttribute;
import model.attribute.ISecondaryAttribute;
import model.item.*;
import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.other.ITalent;
import model.other.Talent;

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
