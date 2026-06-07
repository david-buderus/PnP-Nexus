package de.pnp.manager.component.universe;

import de.pnp.manager.validation.IsValidExpression;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO for {@link CharacterSettings}
 */
public record CharacterSettingsDto(
        @PositiveOrZero int minPrimaryAttributeValue,
        @Positive int maxPrimaryAttributeValue,
        @Positive int maxPrimaryAttributeSum,
        @NotNull @IsValidExpression(expressionType = IsValidExpression.EExpressionType.TIER_FORMULA) String tierFormula,
        @NotNull @IsValidExpression(expressionType = IsValidExpression.EExpressionType.TALENT_POINT_FORMULA) String talentPointFormula
) {

    /**
     * Returns the DTO for the given {@link CharacterSettings}
     */
    public static CharacterSettingsDto from(CharacterSettings settings) {
        return new CharacterSettingsDto(
                settings.getMinPrimaryAttributeValue(),
                settings.getMaxPrimaryAttributeValue(),
                settings.getMaxPrimaryAttributeSum(),
                settings.getTierFormula() != null ? settings.getTierFormula().toHumanReadableString() : "",
                settings.getTalentPointFormula() != null ? settings.getTalentPointFormula().toHumanReadableString() : ""
        );
    }
}