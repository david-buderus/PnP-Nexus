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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * A character in the universe.
 */
public class PnPCharacter extends DatabaseObject {

    @Valid
    @NotNull
    private final CharacterDescription description;

    @DBRef
    @NotNull
    private final Species species;

    @DBRef
    private final Nation nation;

    @NotNull
    private final List<@Valid ICharacterTrait> advantageTraits;

    @NotNull
    private final List<@Valid ICharacterTrait> disadvantageTraits;

    @Valid
    @NotNull
    private final CharacterStats stats;

    @Valid
    @NotNull
    private final CharacterTalents talents;

    @Valid
    @NotNull
    private final CharacterEquipment equipment;

    @Valid
    @NotNull
    private final CharacterInventory inventory;

    @DBRef
    @NotNull
    private final List<Spell> spells;

    @PositiveOrZero
    private int level;

    @PositiveOrZero
    private int experience;

    public PnPCharacter(ObjectId id, CharacterDescription description, Species species, Nation nation,
                        List<ICharacterTrait> advantageTraits, List<ICharacterTrait> disadvantageTraits, CharacterStats stats,
                        CharacterTalents talents, CharacterEquipment equipment, CharacterInventory inventory, List<Spell> spells,
                        int level, int experience) {
        super(id);
        this.description = description;
        this.species = species;
        this.nation = nation;
        this.advantageTraits = advantageTraits;
        this.disadvantageTraits = disadvantageTraits;
        this.stats = stats;
        this.talents = talents;
        this.equipment = equipment;
        this.inventory = inventory;
        this.spells = spells;
        this.level = level;
        this.experience = experience;
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
        builder.addAll(species.getAdvantageTraits()).addAll(species.getDisadvantageTraits());
        if (nation != null) {
            builder.addAll(nation.getAdvantageTraits()).addAll(nation.getDisadvantageTraits());
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

    public Species getSpecies() {
        return species;
    }

    public Nation getNation() {
        return nation;
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

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }
}
