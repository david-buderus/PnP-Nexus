package de.pnp.manager.server.configurator;

import de.pnp.manager.component.Universe;
import de.pnp.manager.server.service.UniverseService;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.server.ResponseStatusException;

/**
 * Creates an empty universe with the name {@link #UNIVERSE_NAME} if the universe is not part of the backup.
 */
@Configurable
public class SimpleUniverseServerConfiguration extends TestServerConfiguratorBase {

    /**
     * The name of the test universe.
     */
    public static final String UNIVERSE_NAME = "test-universe";

    /**
     * The human-readable name of the test universe.
     */
    public static final String UNIVERSE_DISPLAY_NAME = "My Test Universe";

    @Autowired
    private UniverseService universeService;

    public SimpleUniverseServerConfiguration(Path backupZip) {
        super(backupZip);
    }

    @Override
    public void configure() {
        try {
            universeService.getUniverse(UNIVERSE_NAME);
        } catch (ResponseStatusException e) {
            universeService.createUniverse(new Universe(UNIVERSE_NAME, UNIVERSE_DISPLAY_NAME));
        }
    }
}
