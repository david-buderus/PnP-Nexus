package de.pnp.manager.server.database.universe;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.universe.CharacterSettings;
import de.pnp.manager.component.universe.SettingsBase;
import de.pnp.manager.server.UniverseTestBase;
import de.pnp.manager.utils.TestUtils;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link UniverseSettingsRepository}
 */
class UniverseSettingRepositoryTest extends UniverseTestBase {

    @Autowired
    private UniverseSettingsRepository settingsRepository;

    @Test
    void testInsert() {
        CharacterSettings settings = new CharacterSettings(2, 12,
            50, 7, List.of(), List.of());

        settingsRepository.setSettings(getUniverseName(), settings);

        assertThat(settingsRepository.getSettings(getUniverseName(), CharacterSettings.class)).isEqualTo(settings);
    }

    @ParameterizedTest
    @MethodSource("provideAllSettings")
    void testCorrectImplementations(Class<? extends SettingsBase> implClass) {
        assertThat(UniverseSettingsRepository.DEFAULT_SETTINGS.get(implClass)).describedAs(
            "The setting '" + implClass.getSimpleName() + "' needs have a default value.").isNotNull();
        assertThat(settingsRepository.getSettings(getUniverseName(), implClass)).isEqualTo(
            UniverseSettingsRepository.DEFAULT_SETTINGS.get(implClass));
    }

    private static Stream<Arguments> provideAllSettings() {
        return TestUtils.getAllSubClasses(SettingsBase.class).stream()
            .map(clazz -> Arguments.of(Named.of(clazz.getSimpleName(), clazz)));
    }
}