package de.pnp.manager.validation;

import de.pnp.manager.component.character.dto.CharacterStatsDto;
import de.pnp.manager.component.character.dto.PnPCharacterDTO;
import de.pnp.manager.component.math.EReservedVariables;
import de.pnp.manager.component.universe.CharacterSettings;
import de.pnp.manager.server.database.universe.UniverseSettingsRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

/**
 * Validator for item effects.
 */
public class ValidPnPCharacterValidator implements ConstraintValidator<ValidPnPCharacter, PnPCharacterDTO> {

    private final UniverseSettingsRepository settingsRepository;

    public ValidPnPCharacterValidator(@Autowired UniverseSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public boolean isValid(PnPCharacterDTO character, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        Optional<ObjectId> universe = ValidationUtils.getUniverse();
        if (universe.isEmpty()) {
            return true;
        }
        CharacterSettings settings = settingsRepository.getSettings(universe.get(), CharacterSettings.class);

        boolean valid = !hasTooMuchTalentPoints(character, settings, context);

        if (hasTooMuchPrimaryAttributePoints(character, settings, context)) {
            valid = false;
        }

        return valid;
    }

    private boolean hasTooMuchTalentPoints(PnPCharacterDTO character, CharacterSettings settings, ConstraintValidatorContext context) {
        if (settings.getTalentPointFormula() == null) {
            return false;
        }
        double tier = 1;
        if (settings.getTierFormula() != null) {
            tier = Math.round(
                    settings.getTierFormula().calculate(Map.of(
                            EReservedVariables.LEVEL.asVariable(), (double) character.level().level()
                    ))
            );
        }
        long maxTalentPoints = Math.round(settings.getTalentPointFormula().calculate(Map.of(
                EReservedVariables.LEVEL.asVariable(), (double) character.level().level(),
                EReservedVariables.TIER.asVariable(), tier
        )));
        int talentPoints = character.talents().values().stream().mapToInt(PnPCharacterDTO.TalentRollDto::rawValue).sum();
        if (talentPoints <= maxTalentPoints) {
            return false;
        }

        context.buildConstraintViolationWithTemplate("{character.talentPoints}")
                .addPropertyNode("talents").addConstraintViolation();
        return true;
    }

    private boolean hasTooMuchPrimaryAttributePoints(PnPCharacterDTO character, CharacterSettings settings, ConstraintValidatorContext context) {
        int attributePoints = character.stats().primaryStats().values().stream().mapToInt(CharacterStatsDto.StatsDto::rawValue).sum();
        if (attributePoints <= settings.getMaxPrimaryAttributeSum()) {
            return false;
        }

        context.buildConstraintViolationWithTemplate("{character.primaryAttributePoints}")
                .addPropertyNode("stats.primaryStats").addConstraintViolation();
        return true;
    }
}