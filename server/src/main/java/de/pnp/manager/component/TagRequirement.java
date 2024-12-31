package de.pnp.manager.component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import de.pnp.manager.component.item.Item;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Requirements which are defined by multiple possibilities of tags which are needed.
 */
public class TagRequirement {

    /**
     * A requirement which accepts everything
     */
    public final static TagRequirement EMPTY = new TagRequirement(List.of());

    /**
     * One of the given tag sets needs to be a subset of given tags.
     */
    @JsonProperty
    @NotNull
    private final List<@NotEmpty Set<String>> tagRequirements;

    @JsonCreator
    public TagRequirement(List<Set<String>> tagRequirements) {
        this.tagRequirements = tagRequirements;
    }

    /**
     * Checks if the given tags fulfill the requirements.
     */
    public boolean fulfillsRequirements(Set<String> tags) {
        if (tagRequirements.isEmpty()) {
            return true;
        }

        for (Set<String> necessaryTags : tagRequirements) {
            if (tags.containsAll(necessaryTags)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the given {@link  Item} fulfill the requirements.
     *
     * @see #fulfillsRequirements(Set)
     */
    public boolean fulfillsRequirements(Item item) {
        return fulfillsRequirements(item.getTags());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TagRequirement that = (TagRequirement) o;
        return Objects.equals(tagRequirements, that.tagRequirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagRequirements);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("tagRequirements", tagRequirements)
            .toString();
    }
}
