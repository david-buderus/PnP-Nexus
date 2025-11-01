package de.pnp.manager.utils;

import de.pnp.manager.Tag;
import de.pnp.manager.component.EAction;
import de.pnp.manager.component.IResourceUsage;
import de.pnp.manager.component.IResourceUsage.CharacterResourceUsage;
import de.pnp.manager.component.IResourceUsage.ItemUsage;
import de.pnp.manager.component.TagRequirement;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.spell.ECastingType;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.database.SpellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Helper class to create {@link Spell spells}.
 */
public class TestSpellBuilder {

    /**
     * Component wrapper for the {@link TestSpellBuilder}.
     */
    @Component
    public static class TestSpellBuilderFactory {

        @Autowired
        private SpellRepository spellRepository;

        /**
         * Builder with default values.
         */
        public TestSpellBuilder createSpellBuilder(String universe) {
            return new TestSpellBuilder(universe, spellRepository);
        }
    }

    /**
     * Returns a builder without a link to a {@link Universe} or database.
     */
    public static TestSpellBuilder createSpellBuilder() {
        return new TestSpellBuilder(null, null);
    }

    private final String universe;

    private String name;
    private String effect;
    private final List<IResourceUsage<?>> cost;
    private String additionalCost;
    private int castTime;
    private int cooldown;
    private EAction action;
    private Spell.ISpellCast cast;
    private final EnumSet<ECastingType> castingTypes;
    private int tier;
    private final Set<Tag> tags;
    private String countermeasures;

    private boolean shouldGetPersisted;

    private final SpellRepository spellRepository;

    public TestSpellBuilder(String universe, SpellRepository spellRepository) {
        this.universe = universe;
        this.spellRepository = spellRepository;

        this.name = "name";
        this.effect = "effect";
        this.cost = new ArrayList<>();
        this.additionalCost = "addtional cost";
        this.castTime = 0;
        this.cooldown = 1;
        this.action = EAction.ACTION;
        this.cast = new Spell.TagCast(TagRequirement.NO_REQUIREMENT);
        this.castingTypes = EnumSet.noneOf(ECastingType.class);
        this.tier = 1;
        this.tags = new HashSet<>();
        this.countermeasures = "countermeasures";
    }

    /**
     * @see Spell#getName()
     */
    public TestSpellBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see Spell#getEffect()
     */
    public TestSpellBuilder withEffect(String effect) {
        this.effect = effect;
        return this;
    }

    /**
     * @see Spell#getTier()
     */
    public TestSpellBuilder withTier(int tier) {
        this.tier = tier;
        return this;
    }

    /**
     * @see Spell#getCost()
     */
    public TestSpellBuilder withCost(int amount, Item item) {
        this.cost.add(new ItemUsage(amount, item));
        return this;
    }

    /**
     * @see Spell#getCost()
     */
    public TestSpellBuilder withCost(int amount, SecondaryAttribute attribute) {
        this.cost.add(new CharacterResourceUsage(amount, attribute));
        return this;
    }

    /**
     * @see Spell#getCast()
     */
    public TestSpellBuilder withTalents(Talent... talent) {
        this.cast = new Spell.TalentCast(Arrays.asList(talent));
        return this;
    }

    /**
     * @see Spell#getAdditionalCost()
     */
    public TestSpellBuilder withAdditionalCost(String additionalCost) {
        this.additionalCost = additionalCost;
        return this;
    }

    /**
     * @see Spell#getCastTime()
     */
    public TestSpellBuilder withCastTime(int castTime) {
        this.castTime = castTime;
        return this;
    }

    /**
     * @see Spell#getCastingTypes()
     */
    public TestSpellBuilder withCastingType(ECastingType castingType) {
        this.castingTypes.add(castingType);
        return this;
    }

    /**
     * Sets that the resulting {@link Item} will be persisted.
     */
    public TestSpellBuilder persist() {
        this.shouldGetPersisted = true;
        return this;
    }

    /**
     * Creates jewellery matching this builder.
     */
    public Spell build() {
        Spell spell = new Spell(null, name, effect, cost, additionalCost, castTime, cooldown, action, cast,
                castingTypes, tier, tags, countermeasures);
        if (shouldGetPersisted) {
            return spellRepository.insert(universe, spell);
        }
        return spell;
    }
}
