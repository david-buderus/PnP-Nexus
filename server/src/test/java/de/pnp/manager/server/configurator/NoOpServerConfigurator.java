package de.pnp.manager.server.configurator;

import java.nio.file.Path;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Configures nothing on the server.
 */
public class NoOpServerConfigurator extends TestServerConfiguratorBase {

    public NoOpServerConfigurator(@Nullable Path backupZip) {
        super(backupZip);
    }

    @Override
    public void configure() {
        // no op
    }
}
