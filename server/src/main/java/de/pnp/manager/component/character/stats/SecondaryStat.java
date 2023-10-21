package de.pnp.manager.component.character.stats;

import de.pnp.manager.component.StatComputationRules;
import de.pnp.manager.component.attributes.ESecondaryAttribute;
import de.pnp.manager.component.character.CharacterStats;

public class SecondaryStat extends Stat<ESecondaryAttribute> {

    public SecondaryStat(ESecondaryAttribute attribute, int value) {
        super(attribute, value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public float getStatMaxValue(StatComputationRules rules, CharacterStats stats) {
        return rules.getRule(getAttribute()).calculateStat(stats.getPrimaryStats());
    }
}
