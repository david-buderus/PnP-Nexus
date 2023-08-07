package de.pnp.manager.server;

import de.pnp.manager.component.Universe;
import de.pnp.manager.server.service.UniverseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Path;

@Configurable
public class BasicTestServerConfiguration extends TestServerConfiguratorBase {

    public static final String UNIVERSE_NAME = "test-universe";

    public static final String UNIVERSE_DISPLAY_NAME = "My Test Universe";

    @Autowired
    UniverseService universeService;

    public BasicTestServerConfiguration(Path backupZip) {
        super(backupZip);
    }

    @Override
    public void configure() {
        try {
            Universe universe = universeService.getUniverse(UNIVERSE_NAME);
        } catch (ResponseStatusException e) {
            universeService.createUniverse(new Universe(UNIVERSE_NAME, UNIVERSE_DISPLAY_NAME));
        }
    }
}
