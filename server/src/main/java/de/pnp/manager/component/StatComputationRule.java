package de.pnp.manager.component;

import de.pnp.manager.component.attributes.EPrimaryAttribute;
import de.pnp.manager.component.attributes.ESecondaryAttribute;
import de.pnp.manager.component.character.stats.PrimaryStat;
import java.util.Map;
import java.util.Set;

public class StatComputationRule {

    private final ESecondaryAttribute targetAttribute;

    private final Set<PrimaryAttributeSummandRule> dependencies;

    StatComputationRule(ESecondaryAttribute targetAttribute,
        Set<PrimaryAttributeSummandRule> dependencies) {
        this.targetAttribute = targetAttribute;
        this.dependencies = dependencies;
    }

    public int calculateStat(Map<EPrimaryAttribute, PrimaryStat> stats) {
        return Math.round(dependencies.stream().map(dependency -> dependency.getSummand(stats))
            .reduce(0f, Float::sum));
    }

    private static class PrimaryAttributeSummandRule {

        EPrimaryAttribute primaryAttribute;
        float modifier;

        public float getSummand(int attribute) {
            return modifier * attribute;
        }

        public float getSummand(Map<EPrimaryAttribute, PrimaryStat> stats) {
            return getSummand(stats.get(primaryAttribute).getValue());
        }
    }
}
