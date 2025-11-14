package de.pnp.manager.server.configurator;

import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.server.database.universe.UniverseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.File;
import java.util.Map;

/**
 * Creates an empty universe with the ID {@link #UNIVERSE_HEX_ID} if the universe is not part of the backup.
 */
@Configurable
public class SimpleUniverseServerConfiguration extends TestServerConfiguratorBase {

    /**
     * The name of the test universe.
     */
    public static final String UNIVERSE_HEX_ID = "691704f7aaec6c2151805c8e";

    /**
     * The human-readable name of the test universe.
     */
    public static final String UNIVERSE_DISPLAY_NAME = "My Test Universe";

    @Autowired
    private UniverseRepository universeRepository;

    public SimpleUniverseServerConfiguration(File backupZip) {
        super(backupZip);
    }

    @Override
    public Map<ObjectId, ObjectId> configure() {
        ObjectId id = new ObjectId(UNIVERSE_HEX_ID);
        ObjectId mappedId = new ObjectId();
        if (!universeRepository.exists(mappedId)) {
            universeRepository.insert(new Universe(mappedId, UNIVERSE_DISPLAY_NAME));
        }
        return Map.of(id, mappedId);
    }
}
