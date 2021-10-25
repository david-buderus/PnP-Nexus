package de.pnp.manager.testHelper;

import de.pnp.manager.model.manager.Manager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class TestWithManager {

    protected static Manager manager;

    @BeforeAll
    static void beforeAll() throws Exception {
        manager = new Manager();
        Thread.sleep(1000);
    }

    @AfterAll
    static void afterAll() throws Exception {
        manager.close();
    }
}
