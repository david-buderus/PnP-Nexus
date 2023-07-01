package de.pnp.manager.component.character;

import de.pnp.manager.component.StatComputationRules;
import de.pnp.manager.component.attributes.EPrimaryAttribute;
import de.pnp.manager.component.attributes.ESecondaryAttribute;
import de.pnp.manager.component.character.stats.PrimaryStat;
import de.pnp.manager.component.character.stats.SecondaryStat;
import java.util.Collections;
import java.util.Map;

public class CharacterStats {

    private Map<EPrimaryAttribute, PrimaryStat> primaryStats;
    private Map<ESecondaryAttribute, SecondaryStat> secondaryStats;


    public int getStat(EPrimaryAttribute primaryAttribute) {
        return primaryStats.get(primaryAttribute).getStatValue();
    }

    public int getStat(ESecondaryAttribute secondaryAttribute, StatComputationRules rules) {
        return rules.getRule(secondaryAttribute).calculateStat(primaryStats);
    }

    public Map<EPrimaryAttribute, PrimaryStat> getPrimaryStats() {
        return Collections.unmodifiableMap(primaryStats);
    }
}
