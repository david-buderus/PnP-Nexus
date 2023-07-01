package de.pnp.manager.component;

import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.database.SpellRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    private final String name;

    /**
     * The effect of this spell.
     */
    private final String effect;

    /**
     * The cost to cast this spell.
     */
    private final String cost;

    /**
     * The time needed to cast this spell.
     */
    private final String castTime;

    /**
     * The talents needed to cast this spell.
     */
    @DBRef
    private final List<Talent> talents;

    /**
     * The tier of this spell.
     */
    private final int tier;

    public Spell(ObjectId id, String name, String effect, String cost, String castTime,
        List<Talent> talents, int tier) {
        super(id);
        this.name = name;
        this.effect = effect;
        this.cost = cost;
        this.castTime = castTime;
        this.talents = Collections.unmodifiableList(talents);
        this.tier = tier;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public String getCost() {
        return cost;
    }

    public String getCastTime() {
        return castTime;
    }

    public List<Talent> getTalents() {
        return talents;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Spell spell = (Spell) o;
        return getTier() == spell.getTier() && Objects.equals(getName(), spell.getName())
            && Objects.equals(getEffect(), spell.getEffect()) && Objects.equals(getCost(),
            spell.getCost())
            && Objects.equals(getCastTime(), spell.getCastTime()) && Objects.equals(
            getTalents(), spell.getTalents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEffect(), getCost(), getCastTime(), getTalents(),
            getTier());
    }
}
