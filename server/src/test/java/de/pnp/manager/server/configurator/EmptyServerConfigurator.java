package de.pnp.manager.server.configurator;

import org.bson.types.ObjectId;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.util.Map;

/**
 * Configures nothing on the server.
 */
public class EmptyServerConfigurator extends TestServerConfiguratorBase {

    public EmptyServerConfigurator(@Nullable File backupZip) {
        super(backupZip);
    }

    @Override
    public Map<ObjectId, ObjectId> configure() {
        return Map.of();
    }
}
