package de.pnp.manager.server;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * The test configuration for integration tests.
 */
public enum EServerTestConfiguration {
    BASIC_TEST_SERVER(BasicTestServerConfiguration.class, null);

    private final Class<? extends TestServerConfiguratorBase> configuratorClass;
    private final Path backupZip;

    EServerTestConfiguration(@Nullable Class<? extends TestServerConfiguratorBase> configuratorClass,
        @Nullable Path backupZip) {
        this.configuratorClass = configuratorClass;
        this.backupZip = backupZip;
    }

    /**
     * Loads the backup and configures the test server.
     */
    public void setupTestData(ServerTestBase serverTestBase) {
        if (configuratorClass != null) {
            try {
                TestServerConfiguratorBase configurator = configuratorClass.getConstructor(Path.class)
                    .newInstance(backupZip);
                serverTestBase.getBeanFactory().autowireBean(configurator);
                configurator.loadBackup();
                configurator.configure();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                fail(e);
            }
        }
    }
}
