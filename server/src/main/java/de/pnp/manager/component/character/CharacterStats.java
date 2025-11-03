package de.pnp.manager.component.character;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.stats.Stat;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The stats of a {@link PnPCharacter}.
 */
public class CharacterStats {

    @DBRef
    private final Map<PrimaryAttribute, Stat> primaryStats;

    @DBRef
    private final Map<SecondaryAttribute, Stat> secondaryStats;

    public CharacterStats(Map<PrimaryAttribute, Stat> primaryStats, Map<SecondaryAttribute, Stat> secondaryStats) {
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

    public Stat get(PrimaryAttribute attribute) {
        return primaryStats.putIfAbsent(attribute, new Stat(0));
    }

    public Stat get(SecondaryAttribute attribute) {
        return secondaryStats.putIfAbsent(attribute, new Stat(0));
    }

    public void recalculateSecondaryStats(Collection<PrimaryAttribute> primaryAttributes, Collection<SecondaryAttribute> secondaryAttributes) {
        Map<IExpressionVariable, Double> primaryAttributeVariables = primaryAttributes.stream()
                .collect(Collectors.toMap(PrimaryAttributeVariable::new, e -> (double) get(e).getRawValue()));

        for (SecondaryAttribute secondaryAttribute : secondaryAttributes) {
            secondaryStats.put(secondaryAttribute, new Stat(
                            (int) Math.round(secondaryAttribute.getCalculationFormula().calculate(primaryAttributeVariables)),
                            get(secondaryAttribute).getFlatModifier()
                    )
            );
        }
    }
}
