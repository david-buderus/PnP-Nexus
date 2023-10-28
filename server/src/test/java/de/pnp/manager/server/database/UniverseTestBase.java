package de.pnp.manager.server.database;

import de.pnp.manager.component.Universe;
import de.pnp.manager.component.Universe.UniverseSettings;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base test class for every test that needs an existing universe in the database.
 */
@SpringBootTest
public abstract class UniverseTestBase {

    @Autowired
    private UniverseRepository universeRepository;

    /**
     * Database name that should be used for testing
     */
    private final String universeName;

    protected UniverseTestBase() {
        universeName = UUID.randomUUID().toString();
    }

    @BeforeEach
    void setup() {
        universeRepository.insert(new Universe(universeName, universeName));
    }

    @AfterEach
    void tearDown() {
        universeRepository.remove(universeName);
    }

    /**
     * Updates the {@link UniverseSettings} of the {@link Universe test universe}.
     */
    protected void updateUniverseSettings(UniverseSettings settings) {
        Universe universe = universeRepository.get(universeName).orElseThrow();
        universeRepository.update(new Universe(universeName, universe.getDisplayName(), settings));
    }

    protected String getUniverseName() {
        return universeName;
    }
}
