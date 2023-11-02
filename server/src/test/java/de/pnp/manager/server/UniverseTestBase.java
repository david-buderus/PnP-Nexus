package de.pnp.manager.server;

import de.pnp.manager.component.Universe;
import de.pnp.manager.component.Universe.UniverseSettings;
import de.pnp.manager.server.configurator.BasicTestServerConfiguration;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.UniverseRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base test class for every test that needs an existing empty universe in the database.
 */
@TestServer(EServerTestConfiguration.BASIC)
public abstract class UniverseTestBase extends ServerTestBase {

    @Autowired
    private UniverseRepository universeRepository;

    private final String universeName;

    protected UniverseTestBase() {
        universeName = BasicTestServerConfiguration.UNIVERSE_NAME;
    }

    /**
     * Updates the {@link UniverseSettings} of the {@link Universe test universe}.
     */
    protected void updateUniverseSettings(UniverseSettings settings) {
        Universe universe = universeRepository.get(getUniverseName()).orElseThrow();
        universeRepository.update(new Universe(getUniverseName(), universe.getDisplayName(), settings));
    }

    /**
     * Database name that should be used for testing
     */
    protected String getUniverseName() {
        return universeName;
    }
}
