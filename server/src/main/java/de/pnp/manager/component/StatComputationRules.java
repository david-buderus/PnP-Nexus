package de.pnp.manager.component;

import de.pnp.manager.component.attributes.ESecondaryAttribute;
import java.util.Map;
import java.util.Set;

public class StatComputationRules {

  private Map<ESecondaryAttribute, StatComputationRule> statComputationRules;

  public StatComputationRule getRule(ESecondaryAttribute attribute) {
    return statComputationRules.get(attribute);
  }
}
