package de.pnp.manager.server.service.universe;

import de.pnp.manager.component.universe.CharacterSettings;
import de.pnp.manager.component.universe.CurrencySettings;
import de.pnp.manager.component.universe.EquipmentSettings;
import de.pnp.manager.component.universe.ItemSettings;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.server.database.universe.UniverseSettingsRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service to access {@link UniverseSettingsRepository}.
 */
@RestController
@Validated
@RequestMapping("/api/{universe}/universe-settings")
public class UniverseSettingsService {

    @Autowired
    private UniverseSettingsRepository settingsRepository;

    @GetMapping("character")
    @UniverseRead
    @Operation(summary = "Get the settings", operationId = "getCharacterSettings")
    public CharacterSettings getCharacterSettings(@PathVariable String universe) {
        return settingsRepository.getSettings(universe, CharacterSettings.class);
    }

    @PutMapping("character")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the settings", operationId = "updateCharacterSettings")
    public void updateCharacterSettings(@PathVariable String universe, @Valid @RequestBody CharacterSettings settings) {
        settingsRepository.setSettings(universe, settings);
    }

    @GetMapping("currency")
    @UniverseRead
    @Operation(summary = "Get the settings", operationId = "getCurrencySettings")
    public CurrencySettings getCurrencySettings(@PathVariable String universe) {
        return settingsRepository.getSettings(universe, CurrencySettings.class);
    }

    @PutMapping("currency")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the settings", operationId = "updateCurrencySettings")
    public void updateCurrencySettings(@PathVariable String universe, @Valid @RequestBody CurrencySettings settings) {
        settingsRepository.setSettings(universe, settings);
    }

    @GetMapping("item")
    @UniverseRead
    @Operation(summary = "Get the settings", operationId = "getItemSettings")
    public ItemSettings getItemSettings(@PathVariable String universe) {
        return settingsRepository.getSettings(universe, ItemSettings.class);
    }

    @PutMapping("item")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the settings", operationId = "updateItemSettings")
    public void updateItemSettings(@PathVariable String universe, @Valid @RequestBody ItemSettings settings) {
        settingsRepository.setSettings(universe, settings);
    }

    @GetMapping("equipment")
    @UniverseRead
    @Operation(summary = "Get the settings", operationId = "getEquipmentSettings")
    public EquipmentSettings getEquipmentSettings(@PathVariable String universe) {
        return settingsRepository.getSettings(universe, EquipmentSettings.class);
    }

    @PutMapping("equipment")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the settings", operationId = "updateEquipmentSettings")
    public void updateEquipmentSettings(@PathVariable String universe, @Valid @RequestBody EquipmentSettings settings) {
        settingsRepository.setSettings(universe, settings);
    }
}
