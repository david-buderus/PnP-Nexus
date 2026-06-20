package de.pnp.manager.component.character;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.stats.Stat;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The stats of a {@link PnPCharacter}.
 */
public class CharacterStats {

    private final Map<ObjectId, Stat> primaryStats;

    private final Map<ObjectId, Stat> secondaryStats;

    public CharacterStats(Map<ObjectId, Stat> primaryStats, Map<ObjectId, Stat> secondaryStats) {
        this.primaryStats = new HashMap<>(primaryStats);
        this.secondaryStats = new HashMap<>(secondaryStats);
    }

    /**
     * @return the {@link Stat#getValue() value} of the {@link PrimaryAttribute}
     */

    public int getStat(PrimaryAttribute attribute) {
        return get(attribute).getValue();
    }


    /**
     * @return the {@link Stat#getValue() value} of the {@link SecondaryAttribute}
     */
    public int getStat(SecondaryAttribute attribute) {
        return get(attribute).getValue();
    }

    /**
     * Sets the flat modifier of the {@link PrimaryAttribute}
     */
    public void setFlatModifier(PrimaryAttribute attribute, int flatModifier) {
        get(attribute).setFlatModifier(flatModifier);
    }

    /**
     * Sets the flat modifier of the {@link SecondaryAttribute}
     */
    public void setFlatModifier(SecondaryAttribute attribute, int flatModifier) {
        get(attribute).setFlatModifier(flatModifier);
    }

    /**
     * @return the {@link Stat} for the given {@link PrimaryAttribute}
     */
    public Stat get(PrimaryAttribute attribute) {
        return primaryStats.computeIfAbsent(attribute.getId(), a -> new Stat(0));
    }

    /**
     * @return the {@link Stat} for the given {@link SecondaryAttribute}
     */
    public Stat get(SecondaryAttribute attribute) {
        return secondaryStats.computeIfAbsent(attribute.getId(), a -> new Stat(0));
    }

    /**
     * Recalculates the {@link Stat Secondary Stats} in place.
     */
    public void recalculateSecondaryStats(Collection<PrimaryAttribute> primaryAttributes,
                                          Collection<SecondaryAttribute> secondaryAttributes,
                                          Map<IExpressionVariable, Double> extraVariables) {
        Map<IExpressionVariable, Double> primaryAttributeVariables = primaryAttributes.stream()
                .collect(Collectors.toMap(PrimaryAttributeVariable::new, e -> (double) get(e).getRawValue()));
        Map<IExpressionVariable, Double> variables = new HashMap<>();
        variables.putAll(extraVariables);
        variables.putAll(primaryAttributeVariables);

        for (SecondaryAttribute secondaryAttribute : secondaryAttributes) {
            secondaryStats.put(secondaryAttribute.getId(), new Stat(
                            (int) Math.round(secondaryAttribute.getCalculationFormula().calculate(variables)),
                            get(secondaryAttribute).getFlatModifier()
                    )
            );
        }
    }
}
