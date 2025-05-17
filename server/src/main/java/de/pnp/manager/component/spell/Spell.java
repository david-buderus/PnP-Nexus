package de.pnp.manager.component.spell;

import de.pnp.manager.Tag;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.EAction;
import de.pnp.manager.component.IResourceUsage;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.database.SpellRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A concrete spell in the universe.
 */
@Document(SpellRepository.REPOSITORY_NAME)
public class Spell extends DatabaseObject implements IUniquelyNamedDataObject {

    /**
     * The human-readable name of this spell.
     * <p>
     * This entry is always unique.
     */
    @Indexed(unique = true)
    @NotBlank
    private final String name;

    /**
     * The effect of this spell.
     */
    @NotBlank
    private final String effect;

    /**
     * The cost to cast this spell.
     */
    @NotNull
    private final List<IResourceUsage<?>> cost;

    /**
     * The additional cost to cast this spell.
     */
    @NotNull
    private final String additionalCost;

    /**
     * The time needed to cast this spell.
     */
    @PositiveOrZero
    private final int castTime;

    /**
     * The number of rounds the spell needs until it can be cast again.
     */
    @PositiveOrZero
    private final int cooldown;

    /**
     * The type of action this spell needs.
     */
    @NotNull
    private final EAction action;

    /**
     * The talents needed to cast this spell.
     */
    @DBRef
    @NotEmpty
    private final List<Talent> talents;

    /**
     * How to cast this spell.
     */
    @NotNull
    private final EnumSet<ECastingType> castingTypes;

    /**
     * The tier of this spell.
     */
    @NotNull
    @PositiveOrZero
    private final int tier;

    /**
     * The tags of this spell.
     */
    @NotNull
    private final Set<@NotNull Tag> tags;

    /**
     * How the spell can be countered, dodged, ...
     */
    @NotNull
    private final String countermeasures;

    public Spell(ObjectId id, String name, String effect, List<IResourceUsage<?>> cost, String additionalCost,
        int castTime, int cooldown, EAction action, List<Talent> talents, EnumSet<ECastingType> castingTypes, int tier,
        Set<Tag> tags, String countermeasures) {
        super(id);
        this.name = name;
        this.effect = effect;
        this.cost = cost;
        this.additionalCost = additionalCost;
        this.castTime = castTime;
        this.cooldown = cooldown;
        this.action = action;
        this.talents = Collections.unmodifiableList(talents);
        this.castingTypes = castingTypes;
        this.tier = tier;
        this.tags = Collections.unmodifiableSet(tags);
        this.countermeasures = countermeasures;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public List<IResourceUsage<?>> getCost() {
        return cost;
    }

    public String getAdditionalCost() {
        return additionalCost;
    }

    public int getCastTime() {
        return castTime;
    }

    public List<Talent> getTalents() {
        return talents;
    }

    public int getTier() {
        return tier;
    }

    public int getCooldown() {
        return cooldown;
    }

    public EAction getAction() {
        return action;
    }

    public EnumSet<ECastingType> getCastingTypes() {
        return castingTypes;
    }

    public String getCountermeasures() {
        return countermeasures;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Spell spell = (Spell) o;
        return getCastTime() == spell.getCastTime() && getCooldown() == spell.getCooldown()
            && getTier() == spell.getTier()
            && Objects.equals(getName(), spell.getName()) && Objects.equals(getEffect(),
            spell.getEffect()) && Objects.equals(getCost(), spell.getCost()) && Objects.equals(
            getAdditionalCost(), spell.getAdditionalCost()) && getAction() == spell.getAction() && Objects.equals(
            getTalents(), spell.getTalents()) && Objects.equals(getCastingTypes(), spell.getCastingTypes())
            && Objects.equals(getTags(), spell.getTags()) && Objects.equals(getCountermeasures(),
            spell.getCountermeasures());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEffect(), getCost(), getAdditionalCost(), getCastTime(), getCooldown(),
            getAction(), getTalents(), getCastingTypes(), getTier(), getTags(), getCountermeasures());
    }
}
