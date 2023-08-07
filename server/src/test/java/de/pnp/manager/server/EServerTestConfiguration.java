package de.pnp.manager.server;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.nio.file.Path;

public enum EServerTestConfiguration {
    BASIC_TEST_SERVER(BasicTestServerConfiguration.class, 42001, null);
    private final int port;
    private final Class<? extends TestServerConfiguratorBase> configuratorClass;
    private final Path backupZip;

    EServerTestConfiguration(@Nullable Class<? extends TestServerConfiguratorBase> configuratorClass, int port, @Nullable Path backupZip) {
        this.port = port;
        this.configuratorClass = configuratorClass;
        this.backupZip = backupZip;
    }

    public TestServerParameters setupTestData(ServerTestBase serverTestBase) {
        if (configuratorClass != null) {
            try {
                TestServerConfiguratorBase configurator = configuratorClass.getConstructor(Path.class).newInstance(backupZip);
                serverTestBase.getBeanFactory().autowireBean(configurator);
                configurator.loadBackup();
                configurator.configure();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return new TestServerParameters(URI.create("http://localhost:" + port));
    }

    public int getPort() {
        return port;
    }
}
