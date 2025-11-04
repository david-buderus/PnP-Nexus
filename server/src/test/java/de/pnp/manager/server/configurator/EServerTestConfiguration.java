package de.pnp.manager.server.configurator;

import de.pnp.manager.server.ServerTestBase;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * The test configuration for integration tests.
 */
public enum EServerTestConfiguration {

    EMPTY(EmptyServerConfigurator.class, null, null),
    SIMPLE_UNIVERSE(SimpleUniverseServerConfiguration.class, null, SimpleUniverseServerConfiguration.UNIVERSE_NAME),
    BASIC_ITEMS(EmptyServerConfigurator.class, "backups/BasicItems.zip", "basic-items"),
    CHARACTERS(EmptyServerConfigurator.class, "backups/Characters.zip", "character-universe"),
    SPECIES(EmptyServerConfigurator.class, "backups/BasicSpecies.zip", "basic-species");

    private final Class<? extends TestServerConfiguratorBase> configuratorClass;
    private final File backupZip;
    private final String defaultUniverse;

    EServerTestConfiguration(@Nullable Class<? extends TestServerConfiguratorBase> configuratorClass,
                             @Nullable String backupZip, String defaultUniverse) {
        this.configuratorClass = configuratorClass;
        this.defaultUniverse = defaultUniverse;
        if (backupZip == null) {
            this.backupZip = null;
        } else {
            URL resource = getClass().getClassLoader().getResource(backupZip);
            if (resource == null) {
                System.err.println("The backup \"" + backupZip + "\" does not exit");
                this.backupZip = null;
            } else {
                this.backupZip = new File(resource.getFile());
            }
        }
    }

    /**
     * Loads the backup and configures the test server.
     */
    public void setupTestData(ServerTestBase serverTestBase, String importPrefix) {
        if (configuratorClass != null) {
            try {
                TestServerConfiguratorBase configurator = configuratorClass.getConstructor(File.class)
                        .newInstance(backupZip);
                serverTestBase.getBeanFactory().autowireBean(configurator);
                configurator.loadBackup(importPrefix);
                configurator.configure(importPrefix);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                fail(e);
            }
        }
    }

    public String getDefaultUniverse() {
        return defaultUniverse;
    }
}
