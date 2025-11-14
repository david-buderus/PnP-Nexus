package de.pnp.manager.server.database.universe;

import de.pnp.manager.component.universe.CharacterSettings;
import de.pnp.manager.component.universe.SettingsBase;
import de.pnp.manager.server.UniverseTestBase;
import de.pnp.manager.utils.TestUtils;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link UniverseSettingsRepository}
 */
class UniverseSettingRepositoryTest extends UniverseTestBase {

    @Autowired
    private UniverseSettingsRepository settingsRepository;

    @Test
    void testInsert() {
        CharacterSettings settings = new CharacterSettings(2, 12,
                50);

        settingsRepository.setSettings(getUniverseId(), settings);

        assertThat(settingsRepository.getSettings(getUniverseId(), CharacterSettings.class)).isEqualTo(settings);
    }

    @ParameterizedTest
    @MethodSource("provideAllSettings")
    void testCorrectImplementations(Class<? extends SettingsBase> implClass) {
        assertThat(UniverseSettingsRepository.DEFAULT_SETTINGS.get(implClass)).describedAs(
                "The setting '" + implClass.getSimpleName() + "' needs have a default value.").isNotNull();
        assertThat(settingsRepository.getSettings(getUniverseId(), implClass)).isEqualTo(
                UniverseSettingsRepository.DEFAULT_SETTINGS.get(implClass));
    }

    private static Stream<Arguments> provideAllSettings() {
        return TestUtils.getAllSubClasses(SettingsBase.class).stream()
                .map(clazz -> Arguments.of(Named.of(clazz.getSimpleName(), clazz)));
    }
}