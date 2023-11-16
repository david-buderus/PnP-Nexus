package de.pnp.manager.component.character;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.stats.PrimaryStat;
import de.pnp.manager.component.character.stats.SecondaryStat;
import java.util.Collections;
import java.util.Map;

public class CharacterStats {

    private Map<PrimaryAttribute, PrimaryStat> primaryStats;
    private Map<SecondaryAttribute, SecondaryStat> secondaryStats;


    public int getStat(PrimaryStat primaryAttribute) {
        return primaryStats.get(primaryAttribute).getStatValue();
    }


    public Map<PrimaryAttribute, PrimaryStat> getPrimaryStats() {
        return Collections.unmodifiableMap(primaryStats);
    }
}
