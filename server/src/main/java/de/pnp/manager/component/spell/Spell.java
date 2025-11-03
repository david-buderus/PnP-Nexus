package de.pnp.manager.component.spell;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.Tag;
import de.pnp.manager.component.*;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.database.SpellRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

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
    @NotNull
    private final ISpellCast cast;

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
                 int castTime, int cooldown, EAction action, ISpellCast cast, EnumSet<ECastingType> castingTypes, int tier,
                 Set<Tag> tags, String countermeasures) {
        super(id);
        this.name = name;
        this.effect = effect;
        this.cost = cost;
        this.additionalCost = additionalCost;
        this.castTime = castTime;
        this.cooldown = cooldown;
        this.action = action;
        this.cast = cast;
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

    public ISpellCast getCast() {
        return cast;
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
                getCast(), spell.getCast()) && Objects.equals(getCastingTypes(), spell.getCastingTypes())
                && Objects.equals(getTags(), spell.getTags()) && Objects.equals(getCountermeasures(),
                spell.getCountermeasures());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEffect(), getCost(), getAdditionalCost(), getCastTime(), getCooldown(),
                getAction(), getCast(), getCastingTypes(), getTier(), getTags(), getCountermeasures());
    }

    /**
     * Interface how a spell can be cast.
     */
    @JsonSubTypes({
            @JsonSubTypes.Type(value = TalentCast.class, name = "TalentCast"),
            @JsonSubTypes.Type(value = TagCast.class, name = "TagCast")
    })
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    public interface ISpellCast {

    }

    /**
     * A spell cast via a specific talent
     */
    public record TalentCast(@DBRef @NotEmpty List<Talent> talents) implements ISpellCast {
    }

    /**
     * A spell cast via s definition of tags
     */
    public record TagCast(@NotNull TagRequirement tagRequirement) implements ISpellCast {
    }
}
