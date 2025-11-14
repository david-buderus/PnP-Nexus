package de.pnp.manager.server.database.universe;

import com.google.common.annotations.VisibleForTesting;
import de.pnp.manager.component.universe.*;
import de.pnp.manager.exception.UniverseNotFoundException;
import de.pnp.manager.server.database.MongoConfig;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

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
            ItemSettings.class, ItemSettings.DEFAULT,
            EquipmentSettings.class, EquipmentSettings.DEFAULT,
            CharacterSheetSettings.class, CharacterSheetSettings.DEFAULT
    );

    private final MongoConfig config;

    private final UniverseRepository universeRepository;

    public UniverseSettingsRepository(@Autowired MongoConfig config, @Autowired UniverseRepository universeRepository) {
        this.config = config;
        this.universeRepository = universeRepository;
    }

    /**
     * Returns the settings or the default value of the settings.
     */
    public <S extends SettingsBase> S getSettings(ObjectId universe, Class<S> clazz) {
        S settings = getTemplate(universe).findById(clazz.getName(), clazz, REPOSITORY_NAME);
        if (settings == null) {
            return clazz.cast(DEFAULT_SETTINGS.get(clazz));
        }
        return settings;
    }

    /**
     * Sets the settings.
     */
    public <S extends SettingsBase> void setSettings(ObjectId universe, S settings) {
        getTemplate(universe).save(settings, REPOSITORY_NAME);
    }

    /**
     * Returns the {@link MongoTemplate} to manipulate the database of the given universe.
     */
    protected MongoTemplate getTemplate(ObjectId universe) {
        if (!universeRepository.exists(universe)) {
            throw new UniverseNotFoundException(universe);
        }
        return config.universeMongoTemplate(universe);
    }
}