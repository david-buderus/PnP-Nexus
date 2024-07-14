package de.pnp.manager.server.database.universe;

import com.google.common.annotations.VisibleForTesting;
import de.pnp.manager.component.universe.CharacterSettings;
import de.pnp.manager.component.universe.CurrencySettings;
import de.pnp.manager.component.universe.ItemSettings;
import de.pnp.manager.component.universe.SettingsBase;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.exception.UniverseNotFoundException;
import de.pnp.manager.server.database.MongoConfig;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * Stores the settings of a {@link Universe}
 */
@Component
public class UniverseSettingsRepository {

    /**
     * Name of the repository.
     */
    public static final String REPOSITORY_NAME = "universe-settings";

    @VisibleForTesting
    static final Map<Class<? extends SettingsBase>, SettingsBase> DEFAULT_SETTINGS = Map.of(
        CharacterSettings.class, CharacterSettings.DEFAULT,
        CurrencySettings.class, CurrencySettings.DEFAULT,
        ItemSettings.class, ItemSettings.DEFAULT
    );

    @Autowired
    private MongoConfig config;

    @Autowired
    private UniverseRepository universeRepository;

    /**
     * Returns the settings or the default value of the settings.
     */
    public <S extends SettingsBase> S getSettings(String universe, Class<S> clazz) {
        S settings = getTemplate(universe).findById(clazz.getName(), clazz, REPOSITORY_NAME);
        if (settings == null) {
            return clazz.cast(DEFAULT_SETTINGS.get(clazz));
        }
        return settings;
    }

    /**
     * Sets the settings.
     */
    public <S extends SettingsBase> void setSettings(String universe, S settings) {
        getTemplate(universe).save(settings, REPOSITORY_NAME);
    }

    /**
     * Returns the {@link MongoTemplate} to manipulate the database of the given universe.
     */
    protected MongoTemplate getTemplate(String universe) {
        if (!universeRepository.exists(universe)) {
            throw new UniverseNotFoundException(universe);
        }
        return config.universeMongoTemplate(universe);
    }
}