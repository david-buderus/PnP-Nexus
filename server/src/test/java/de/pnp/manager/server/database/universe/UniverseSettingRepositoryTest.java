package de.pnp.manager.server.database.universe;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.universe.CharacterSettings;
import de.pnp.manager.server.UniverseTestBase;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UniverseSettingRepositoryTest extends UniverseTestBase {

    @Autowired
    private UniverseSettingsRepository settingsRepository;

    @Test
    void testInsert() {
        CharacterSettings settings = new CharacterSettings(7, List.of(), List.of());

        settingsRepository.setSettings(getUniverseName(), settings);

        assertThat(settingsRepository.getSettings(getUniverseName(), CharacterSettings.class)).isEqualTo(settings);
    }
}