package de.pnp.manager.server;

import de.pnp.manager.component.universe.SettingsBase;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.universe.UniverseSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base test class for every test that needs an existing empty universe in the database.
 */
@TestServer(EServerTestConfiguration.SIMPLE_UNIVERSE)
public abstract class UniverseTestBase extends ServerTestBase {

    @Autowired
    private UniverseSettingsRepository settingsRepository;

    /**
     * Updates the {@link SettingsBase} of the {@link Universe test universe}.
     */
    protected void updateUniverseSettings(SettingsBase settings) {
        settingsRepository.setSettings(getUniverseName(), settings);
    }
}
