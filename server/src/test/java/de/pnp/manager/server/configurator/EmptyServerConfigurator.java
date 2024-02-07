package de.pnp.manager.server.configurator;

import java.io.File;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Configures nothing on the server.
 */
public class EmptyServerConfigurator extends TestServerConfiguratorBase {

    public EmptyServerConfigurator(@Nullable File backupZip) {
        super(backupZip);
    }

    @Override
    public void configure() {
        // NO OP
    }
}
