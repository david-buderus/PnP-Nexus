package de.pnp.manager.component.character;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.stats.Stat;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The stats of a {@link PnPCharacter}.
 */
public class CharacterStats {

    @NotEmpty
    private final Map<PrimaryAttribute, Stat> primaryStats;

    @NotEmpty
    private final Map<SecondaryAttribute, Stat> secondaryStats;

    @JsonCreator
    public CharacterStats(Map<PrimaryAttribute, Stat> primaryStats, Map<SecondaryAttribute, Stat> secondaryStats) {
        this.primaryStats = new HashMap<>(primaryStats);
        this.secondaryStats = new HashMap<>(secondaryStats);
    }

    public int getStat(PrimaryAttribute attribute) {
        return primaryStats.get(attribute).getValue();
    }


    public int getStat(SecondaryAttribute attribute) {
        return secondaryStats.get(attribute).getValue();
    }

    public void setFlatModifier(PrimaryAttribute attribute, int flatModifier) {
        primaryStats.get(attribute).setFlatModifier(flatModifier);
    }

    public void setFlatModifier(SecondaryAttribute attribute, int flatModifier) {
        secondaryStats.get(attribute).setFlatModifier(flatModifier);
    }

    public void recalculateSecondaryStats() {
        for (Entry<SecondaryAttribute, Stat> entry : secondaryStats.entrySet()) {
            SecondaryAttribute attribute = entry.getKey();
            secondaryStats.put(attribute, new Stat((int) Math.round(attribute.getPrimaryAttributeDependencies().stream()
                .mapToDouble(dependency -> dependency.factor() * getStat(dependency.primaryAttribute())).sum()),
                entry.getValue().getFlatModifier()));
        }
    }
}
