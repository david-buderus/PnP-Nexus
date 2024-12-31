package de.pnp.manager.component;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.utils.TestItemBuilder;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TagRequirement}
 */
class TagRequirementTest {

    @ParameterizedTest
    @MethodSource("provideTags")
    void fulfillsRequirements(Set<String> tags, boolean expected) {
        TagRequirement requirement = new TagRequirement(List.of(Set.of("E1", "E3"), Set.of("E2"), Set.of("E1", "E4")));
        assertThat(requirement.fulfillsRequirements(tags)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideTags")
    void emptyFulfillsRequirements(Set<String> tags) {
        assertThat(TagRequirement.EMPTY.fulfillsRequirements(tags)).isTrue();
    }

    @Test
    void testFulfillsRequirements() {
        TagRequirement requirement = new TagRequirement(List.of(Set.of("Example 1", "Example 3"), Set.of("Example 2")));
        Item item1 = TestItemBuilder.createItemBuilder().withTags("Example 1", "Example 2").buildItem();
        Item item2 = TestItemBuilder.createItemBuilder().withTags("Example 3").buildItem();

        assertThat(requirement.fulfillsRequirements(item1)).isTrue();
        assertThat(requirement.fulfillsRequirements(item2)).isFalse();
    }

    private static Stream<Arguments> provideTags() {
        return Stream.of(
            Arguments.of(Set.of("E2"), true),
            Arguments.of(Set.of("E1"), false),
            Arguments.of(Set.of("E3"), false),
            Arguments.of(Set.of("E4"), false),
            Arguments.of(Set.of("E5"), false),
            Arguments.of(Set.of("E1", "E3"), true),
            Arguments.of(Set.of("E1", "E2"), true),
            Arguments.of(Set.of("E3", "E4"), false),
            Arguments.of(Set.of("E1", "E4"), true)
        );
    }
}