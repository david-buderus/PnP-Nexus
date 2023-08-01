package de.pnp.manager.component.character.stats;

import de.pnp.manager.component.StatComputationRules;
import de.pnp.manager.component.attributes.ESecondaryAttribute;
import de.pnp.manager.component.character.CharacterStats;

public class SecondaryStat {

    ESecondaryAttribute attribute;

    public SecondaryStat(ESecondaryAttribute attribute) {
        this.attribute = attribute;
    }

    public float getStatMaxValue(StatComputationRules rules, CharacterStats stats) {
        return rules.getRule(attribute).calculateStat(stats.getPrimaryStats());
    }
}
