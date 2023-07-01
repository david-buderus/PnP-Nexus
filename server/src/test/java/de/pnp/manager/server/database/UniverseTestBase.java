package de.pnp.manager.server.database;

import de.pnp.manager.component.Universe;
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

    /**
     * Database name that should be used for testing
     */
    protected final String universeName;


    protected UniverseTestBase() {
        universeName = UUID.randomUUID().toString();
    }

    @BeforeEach
    void setup(@Autowired UniverseRepository repository) {
        repository.insert(new Universe(universeName, universeName));
    }

    @AfterEach
    void tearDown(@Autowired UniverseRepository repository) {
        repository.remove(universeName);
    }
}
