package de.pnp.manager.component.universe;

import org.springframework.data.annotation.Id;

/**
 * Base for universe settings.
 * <p>
 * Automatically sets the id to the name of the class.
 */
public abstract class SettingsBase {

    @Id
    @SuppressWarnings({"unused", "FieldMayBeFinal"}) // Used for identification in the database
    private String id = getClass().getName();
}
