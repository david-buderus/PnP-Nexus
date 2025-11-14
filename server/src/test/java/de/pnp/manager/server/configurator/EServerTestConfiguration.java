package de.pnp.manager.server.configurator;

import de.pnp.manager.server.ServerTestBase;
import org.bson.types.ObjectId;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * The test configuration for integration tests.
 */
public enum EServerTestConfiguration {

    EMPTY(EmptyServerConfigurator.class, null, null),
    SIMPLE_UNIVERSE(SimpleUniverseServerConfiguration.class, null, SimpleUniverseServerConfiguration.UNIVERSE_HEX_ID),
    BASIC_ITEMS(EmptyServerConfigurator.class, "backups/BasicItems.zip", "691704f7aaec6c2151805c92"),
    CHARACTERS(EmptyServerConfigurator.class, "backups/Characters.zip", "691704f7aaec6c2151805c93"),
    SPECIES(EmptyServerConfigurator.class, "backups/BasicSpecies.zip", "691704f7aaec6c2151805c94");

    private final Class<? extends TestServerConfiguratorBase> configuratorClass;
    private final File backupZip;
    private final ObjectId defaultUniverse;

    EServerTestConfiguration(@Nullable Class<? extends TestServerConfiguratorBase> configuratorClass,
                             @Nullable String backupZip, @Nullable String defaultUniverse) {
        this.configuratorClass = configuratorClass;
        this.defaultUniverse = defaultUniverse != null ? new ObjectId(defaultUniverse) : null;
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
    public Map<ObjectId, ObjectId> setupTestData(ServerTestBase serverTestBase) {
        if (configuratorClass == null) {
            return Map.of();
        }
        try {
            TestServerConfiguratorBase configurator = configuratorClass.getConstructor(File.class)
                    .newInstance(backupZip);
            serverTestBase.getBeanFactory().autowireBean(configurator);
            Map<ObjectId, ObjectId> remapping = new HashMap<>();
            remapping.putAll(configurator.loadBackup());
            remapping.putAll(configurator.configure());
            return remapping;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            return fail(e);
        }
    }

    public ObjectId getDefaultUniverse() {
        return defaultUniverse;
    }
}
