package de.pnp.manager.testHelper;

import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.ServerNetworkHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.assertj.core.api.Assertions.assertThat;

public class TestWithManager {

    protected static Manager manager;

    @BeforeAll
    static void beforeAll() throws Exception {
        manager = new Manager();
        Thread.sleep(1000);
        assertThat(((ServerNetworkHandler) manager.getNetworkHandler()).isActive()).isTrue();
    }

    @AfterAll
    static void afterAll() throws Exception {
        manager.close();
    }
}
