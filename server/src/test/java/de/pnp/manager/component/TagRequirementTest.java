package de.pnp.manager.component;

import de.pnp.manager.Tag;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.utils.TestItemBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static de.pnp.manager.utils.TestUtils.tagSet;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TagRequirement}
 */
class TagRequirementTest {

    @ParameterizedTest
    @MethodSource("provideTags")
    void fulfillsRequirements(Set<Tag> tags, boolean expected) {
        TagRequirement requirement = TagRequirement.from(List.of(Set.of("E1", "E3"), Set.of("E2"), Set.of("E1", "E4")));
        assertThat(requirement.fulfillsRequirements(tags)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideTags")
    void emptyFulfillsRequirements(Set<Tag> tags) {
        assertThat(TagRequirement.NO_REQUIREMENT.fulfillsRequirements(tags)).isTrue();
    }

    @Test
    void testFulfillsRequirements() {
        TagRequirement requirement = TagRequirement.from(
                List.of(Set.of("Example 1", "Example 3"), Set.of("Example 2")));
        Item item1 = TestItemBuilder.createItemBuilder().withTags("Example 1", "Example 2").buildItem();
        Item item2 = TestItemBuilder.createItemBuilder().withTags("Example 3").buildItem();

        assertThat(requirement.fulfillsRequirements(item1)).isTrue();
        assertThat(requirement.fulfillsRequirements(item2)).isFalse();
    }

    private static Stream<Arguments> provideTags() {
        return Stream.of(
                Arguments.of(tagSet("E2"), true),
                Arguments.of(tagSet("E1"), false),
                Arguments.of(tagSet("E3"), false),
                Arguments.of(tagSet("E4"), false),
                Arguments.of(tagSet("E5"), false),
                Arguments.of(tagSet("E1", "E3"), true),
                Arguments.of(tagSet("E1", "E2"), true),
                Arguments.of(tagSet("E3", "E4"), false),
                Arguments.of(tagSet("E1", "E4"), true)
        );
    }
}