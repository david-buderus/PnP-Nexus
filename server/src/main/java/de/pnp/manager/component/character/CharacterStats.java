package de.pnp.manager.component.character;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.stats.Stat;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import jakarta.validation.constraints.NotEmpty;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The stats of a {@link PnPCharacter}.
 */
public class CharacterStats {

    @NotEmpty
    @JsonProperty("primaryStats")
    private final Map<ObjectId, Stat> primaryStats;

    @NotEmpty
    @JsonProperty("secondaryStats")
    private final Map<ObjectId, Stat> secondaryStats;

    @JsonCreator
    public CharacterStats(Map<ObjectId, Stat> primaryStats, Map<ObjectId, Stat> secondaryStats) {
        this.primaryStats = new HashMap<>(primaryStats);
        this.secondaryStats = new HashMap<>(secondaryStats);
    }

    public int getStat(PrimaryAttribute attribute) {
        return get(attribute).getValue();
    }


    public int getStat(SecondaryAttribute attribute) {
        return get(attribute).getValue();
    }

    public void setFlatModifier(PrimaryAttribute attribute, int flatModifier) {
        get(attribute).setFlatModifier(flatModifier);
    }

    public void setFlatModifier(SecondaryAttribute attribute, int flatModifier) {
        get(attribute).setFlatModifier(flatModifier);
    }

    private Stat get(PrimaryAttribute attribute) {
        return primaryStats.putIfAbsent(attribute.getId(), new Stat(0));
    }

    private Stat get(SecondaryAttribute attribute) {
        return secondaryStats.putIfAbsent(attribute.getId(), new Stat(0));
    }

    public void recalculateSecondaryStats(List<PrimaryAttribute> primaryAttributes, List<SecondaryAttribute> secondaryAttributes) {
        Map<IExpressionVariable, Double> primaryAttributeVariables = primaryAttributes.stream()
                .collect(Collectors.toMap(PrimaryAttributeVariable::new, e -> (double) get(e).getRawValue()));

        for (SecondaryAttribute secondaryAttribute : secondaryAttributes) {
            secondaryStats.put(secondaryAttribute.getId(), new Stat(
                            (int) Math.round(secondaryAttribute.getCalculationFormula().calculate(primaryAttributeVariables)),
                            get(secondaryAttribute).getFlatModifier()
                    )
            );
        }
    }
}
