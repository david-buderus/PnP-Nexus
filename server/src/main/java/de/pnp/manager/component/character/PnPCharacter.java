package de.pnp.manager.component.character;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.traits.ICharacterTrait;
import de.pnp.manager.component.character.traits.StatTrait.PrimaryStatTrait;
import de.pnp.manager.component.character.traits.StatTrait.SecondaryStatTrait;
import de.pnp.manager.component.character.traits.TalentCharacterTrait;
import de.pnp.manager.component.spell.Spell;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;
import java.util.Map;

/**
 * A character in the universe.
 */
public class PnPCharacter extends DatabaseObject {

    private final CharacterDescription description;

    private final CharacterLevel level;

    private final CharacterOrigin origin;

    private final List<ICharacterTrait> advantageTraits;

    private final List<ICharacterTrait> disadvantageTraits;

    private final CharacterStats stats;

    private final CharacterTalents talents;

    private final CharacterEquipment equipment;

    private final CharacterInventory inventory;

    @DBRef
    private final List<Spell> spells;

    private final Map<String, String> customFields;

    public PnPCharacter(ObjectId id, CharacterDescription description, CharacterLevel level, CharacterOrigin origin,
                        List<ICharacterTrait> advantageTraits, List<ICharacterTrait> disadvantageTraits, CharacterStats stats,
                        CharacterTalents talents, CharacterEquipment equipment, CharacterInventory inventory, List<Spell> spells, Map<String, String> customFields) {
        super(id);
        this.description = description;
        this.level = level;
        this.origin = origin;
        this.advantageTraits = advantageTraits;
        this.disadvantageTraits = disadvantageTraits;
        this.stats = stats;
        this.talents = talents;
        this.equipment = equipment;
        this.inventory = inventory;
        this.spells = spells;
        this.customFields = customFields;
    }

    public int getTalentRoll(Talent talent) {
        int roll = talents.getRoll(talent);
        for (TalentCharacterTrait trait : getAllTalentTraits()) {
            roll = trait.apply(talent, roll);
        }
        return roll;
    }

    public int getPrimaryStat(PrimaryAttribute attribute) {
        float stat = stats.getStat(attribute);
        for (PrimaryStatTrait trait : getAllPrimaryStatTraits()) {
            stat = trait.apply(attribute, stat);
        }
        return Math.round(stat);
    }

    public int getSecondaryStat(SecondaryAttribute attribute) {
        float stat = stats.getStat(attribute);
        for (SecondaryStatTrait trait : getAllSecondaryStatTraits()) {
            stat = trait.apply(attribute, stat);
        }
        return Math.round(stat);
    }

    public List<ICharacterTrait> getAllTraits() {
        Builder<ICharacterTrait> builder = ImmutableList.builder();
        builder.addAll(advantageTraits).addAll(disadvantageTraits);
        if (origin.species() != null) {
            builder.addAll(origin.species().getAdvantageTraits()).addAll(origin.species().getDisadvantageTraits());
        }
        if (origin.nation() != null) {
            builder.addAll(origin.nation().getAdvantageTraits()).addAll(origin.nation().getDisadvantageTraits());
        }
        return builder.build();
    }

    public List<TalentCharacterTrait> getAllTalentTraits() {
        return getAllTraits().stream()
                .filter(TalentCharacterTrait.class::isInstance).map(TalentCharacterTrait.class::cast).toList();
    }

    public List<PrimaryStatTrait> getAllPrimaryStatTraits() {
        return getAllTraits().stream()
                .filter(PrimaryStatTrait.class::isInstance).map(PrimaryStatTrait.class::cast).toList();
    }

    public List<SecondaryStatTrait> getAllSecondaryStatTraits() {
        return getAllTraits().stream()
                .filter(SecondaryStatTrait.class::isInstance).map(SecondaryStatTrait.class::cast).toList();
    }

    public CharacterDescription getDescription() {
        return description;
    }

    public CharacterOrigin getOrigin() {
        return origin;
    }

    public List<ICharacterTrait> getAdvantageTraits() {
        return advantageTraits;
    }

    public List<ICharacterTrait> getDisadvantageTraits() {
        return disadvantageTraits;
    }

    public CharacterStats getStats() {
        return stats;
    }

    public CharacterTalents getTalents() {
        return talents;
    }

    public CharacterEquipment getEquipment() {
        return equipment;
    }

    public CharacterInventory getInventory() {
        return inventory;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public CharacterLevel getLevel() {
        return level;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }
}
