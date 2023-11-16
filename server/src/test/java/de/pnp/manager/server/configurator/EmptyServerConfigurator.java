package de.pnp.manager.server.configurator;

import java.nio.file.Path;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Configures nothing on the server.
 */
public class EmptyServerConfigurator extends TestServerConfiguratorBase {

    public EmptyServerConfigurator(@Nullable Path backupZip) {
        super(backupZip);
    }

    @Override
    public void configure() {
        // NO OP
    }
}
