package de.pnp.manager.server;

import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.component.universe.UniverseSettings;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.UniverseRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base test class for every test that needs an existing empty universe in the database.
 */
@TestServer(EServerTestConfiguration.SIMPLE_UNIVERSE)
public abstract class UniverseTestBase extends ServerTestBase {

    @Autowired
    private UniverseRepository universeRepository;

    /**
     * Updates the {@link UniverseSettings} of the {@link Universe test universe}.
     */
    protected void updateUniverseSettings(UniverseSettings settings) {
        Universe universe = universeRepository.get(getUniverseName()).orElseThrow();
        universeRepository.update(new Universe(getUniverseName(), universe.getDisplayName(), settings));
    }
}
