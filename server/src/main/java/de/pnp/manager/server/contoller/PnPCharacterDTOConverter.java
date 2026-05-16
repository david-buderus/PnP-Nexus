package de.pnp.manager.server.contoller;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.CharacterStats;
import de.pnp.manager.component.character.CharacterTalents;
import de.pnp.manager.component.character.PnPCharacter;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.component.character.dto.CharacterStatsDto;
import de.pnp.manager.component.character.dto.PnPCharacterDTO;
import de.pnp.manager.component.character.stats.Stat;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * A controller to convert {@link PnPCharacterDTO} and {@link PnPCharacter}.
 */
@Component
public class PnPCharacterDTOConverter {

    private final PrimaryAttributeRepository primaryAttributeRepository;
    private final SecondaryAttributeRepository secondaryAttributeRepository;
    private final TalentRepository talentRepository;

    public PnPCharacterDTOConverter(@Autowired PrimaryAttributeRepository primaryAttributeRepository,
                                    @Autowired SecondaryAttributeRepository secondaryAttributeRepository,
                                    @Autowired TalentRepository talentRepository) {
        this.primaryAttributeRepository = primaryAttributeRepository;
        this.secondaryAttributeRepository = secondaryAttributeRepository;
        this.talentRepository = talentRepository;
    }


    /**
     * Converts a {@link List} of {@link PnPCharacterDTO} to a {@link List} of {@link PnPCharacter}
     */
    public List<PnPCharacter> convertFromDto(ObjectId universe, List<PnPCharacterDTO> characterDTOs) {
        Collection<PrimaryAttribute> primaryAttributes = primaryAttributeRepository.getAll(universe);
        Collection<SecondaryAttribute> secondaryAttributes = secondaryAttributeRepository.getAll(universe);
        Collection<Talent> talents = talentRepository.getAll(universe);

        List<PnPCharacter> characters = new ArrayList<>();

        for (PnPCharacterDTO character : characterDTOs) {
            characters.add(convert(character, primaryAttributes, secondaryAttributes, talents));
        }

        return characters;
    }

    /**
     * Converts a {@link PnPCharacterDTO} to a {@link PnPCharacter}
     */
    public PnPCharacter convert(ObjectId universe, PnPCharacterDTO character) {
        Collection<PrimaryAttribute> primaryAttributes = primaryAttributeRepository.getAll(universe);
        Collection<SecondaryAttribute> secondaryAttributes = secondaryAttributeRepository.getAll(universe);
        Collection<Talent> talents = talentRepository.getAll(universe);

        return convert(character, primaryAttributes, secondaryAttributes, talents);
    }

    private PnPCharacter convert(PnPCharacterDTO character, Collection<PrimaryAttribute> primaryAttributes,
                                 Collection<SecondaryAttribute> secondaryAttributes,
                                 Collection<Talent> talents) {
        return new PnPCharacter(
                character.id(),
                character.description(),
                character.level(),
                character.origin(),
                character.advantageTraits(),
                character.disadvantageTraits(),
                convert(character.stats(), primaryAttributes, secondaryAttributes),
                convert(character.talents(), talents),
                character.equipment(),
                character.inventory(),
                character.spells(),
                character.customFields()
        );
    }

    private CharacterStats convert(CharacterStatsDto characterStatsDto, Collection<PrimaryAttribute> primaryAttributes,
                                   Collection<SecondaryAttribute> secondaryAttributes) {
        Map<ObjectId, Stat> primaryStats = new HashMap<>();
        Map<ObjectId, Stat> secondaryStats = new HashMap<>();

        for (PrimaryAttribute primaryAttribute : primaryAttributes) {
            CharacterStatsDto.StatsDto stats = characterStatsDto.primaryStats().get(primaryAttribute.getId());
            if (stats == null) {
                continue;
            }
            primaryStats.put(primaryAttribute.getId(), new Stat(stats.rawValue(), stats.flatModifier()));
        }
        for (SecondaryAttribute secondaryAttribute : secondaryAttributes) {
            CharacterStatsDto.StatsDto stats = characterStatsDto.secondaryStats().get(secondaryAttribute.getId());
            if (stats == null) {
                continue;
            }
            secondaryStats.put(secondaryAttribute.getId(), new Stat(stats.rawValue(), stats.flatModifier()));
        }

        CharacterStats characterStats = new CharacterStats(primaryStats, secondaryStats);
        characterStats.recalculateSecondaryStats(primaryAttributes, secondaryAttributes);
        return characterStats;
    }

    private CharacterTalents convert(Map<ObjectId, PnPCharacterDTO.TalentRollDto> talentsDto, Collection<Talent> talents) {
        CharacterTalents characterTalents = new CharacterTalents(new HashMap<>());

        for (Talent talent : talents) {
            PnPCharacterDTO.TalentRollDto talentRoll = talentsDto.get(talent.getId());
            if (talentRoll == null) {
                continue;
            }
            characterTalents.setRoll(talent, talentRoll.rawValue());
        }

        return characterTalents;
    }

    /**
     * Converts a {@link List} of {@link PnPCharacter} to a {@link List} of {@link PnPCharacterDTO}
     */
    public List<PnPCharacterDTO> convert(ObjectId universe, Collection<PnPCharacter> characters) {
        Collection<PrimaryAttribute> primaryAttributes = primaryAttributeRepository.getAll(universe);
        Collection<SecondaryAttribute> secondaryAttributes = secondaryAttributeRepository.getAll(universe);
        Collection<Talent> talents = talentRepository.getAll(universe);

        List<PnPCharacterDTO> dtos = new ArrayList<>();

        for (PnPCharacter character : characters) {
            dtos.add(convert(character, primaryAttributes, secondaryAttributes, talents));
        }

        return dtos;
    }

    /**
     * Converts a {@link PnPCharacter} to a {@link PnPCharacterDTO}
     */
    public PnPCharacterDTO convert(ObjectId universe, PnPCharacter character) {
        Collection<PrimaryAttribute> primaryAttributes = primaryAttributeRepository.getAll(universe);
        Collection<SecondaryAttribute> secondaryAttributes = secondaryAttributeRepository.getAll(universe);
        Collection<Talent> talents = talentRepository.getAll(universe);

        return convert(character, primaryAttributes, secondaryAttributes, talents);
    }

    private PnPCharacterDTO convert(PnPCharacter character, Collection<PrimaryAttribute> primaryAttributes,
                                    Collection<SecondaryAttribute> secondaryAttributes,
                                    Collection<Talent> talents) {
        Map<ObjectId, CharacterStatsDto.StatsDto> primaryStats = new HashMap<>();
        for (PrimaryAttribute attribute : primaryAttributes) {
            primaryStats.put(attribute.getId(), new CharacterStatsDto.StatsDto(
                    character.getStats().get(attribute).getRawValue(),
                    character.getStats().get(attribute).getFlatModifier(),
                    character.getPrimaryStat(attribute)
            ));
        }

        Map<ObjectId, CharacterStatsDto.StatsDto> secondaryStats = new HashMap<>();
        for (SecondaryAttribute attribute : secondaryAttributes) {
            secondaryStats.put(attribute.getId(), new CharacterStatsDto.StatsDto(
                    character.getStats().get(attribute).getRawValue(),
                    character.getStats().get(attribute).getFlatModifier(),
                    character.getSecondaryStat(attribute)
            ));
        }

        Map<ObjectId, PnPCharacterDTO.TalentRollDto> talentDto = new HashMap<>();
        for (Talent talent : talents) {
            talentDto.put(talent.getId(), new PnPCharacterDTO.TalentRollDto(
                    character.getTalents().getRoll(talent),
                    character.getTalentRoll(talent)
            ));
        }

        return new PnPCharacterDTO(
                character.getId(),
                character.getDescription(),
                character.getLevel(),
                character.getOrigin(),
                character.getAdvantageTraits(),
                character.getDisadvantageTraits(),
                new CharacterStatsDto(primaryStats, secondaryStats),
                talentDto,
                character.getEquipment(),
                character.getInventory(),
                character.getSpells(),
                character.getCustomFields()
        );
    }

    /**
     * @return Recalculates all entries with dependencies in the {@link PnPCharacterDTO}.
     */
    public PnPCharacterDTO recalculateEntries(ObjectId universe, PnPCharacterDTO character) {
        Collection<PrimaryAttribute> primaryAttributes = primaryAttributeRepository.getAll(universe);
        Collection<SecondaryAttribute> secondaryAttributes = secondaryAttributeRepository.getAll(universe);
        Collection<Talent> talents = talentRepository.getAll(universe);

        return convert(convert(character, primaryAttributes, secondaryAttributes, talents), primaryAttributes, secondaryAttributes, talents);
    }
}
