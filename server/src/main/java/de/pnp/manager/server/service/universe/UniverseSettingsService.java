package de.pnp.manager.server.service.universe;

import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IllegalFormulaException;
import de.pnp.manager.component.universe.*;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.server.database.universe.UniverseSettingsRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

/**
 * Service to access {@link UniverseSettingsRepository}.
 */
@RestController
@Validated
@RequestMapping("/api/{universe}/universe-settings")
public class UniverseSettingsService {

    private final UniverseSettingsRepository settingsRepository;

    public UniverseSettingsService(@Autowired UniverseSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @GetMapping("character")
    @UniverseRead
    @Operation(summary = "Get the settings", operationId = "getCharacterSettings")
    public CharacterSettings getCharacterSettings(@PathVariable ObjectId universe) {
        return settingsRepository.getSettings(universe, CharacterSettings.class);
    }

    @PutMapping("character")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the settings", operationId = "updateCharacterSettings")
    public void updateCharacterSettings(@PathVariable ObjectId universe, @Valid @RequestBody CharacterSettings settings) {
        settingsRepository.setSettings(universe, settings);
    }

    @GetMapping("character/dto")
    @UniverseRead
    @Operation(summary = "Get the settings", operationId = "getCharacterSettingsDto")
    public CharacterSettingsDto getCharacterSettingsDto(@PathVariable ObjectId universe) {
        return CharacterSettingsDto.from(settingsRepository.getSettings(universe, CharacterSettings.class));
    }

    @PutMapping("character/dto")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the settings", operationId = "updateCharacterSettingsDto")
    public void updateCharacterSettingsDto(@PathVariable ObjectId universe, @Valid @RequestBody CharacterSettingsDto settings) {
        try {
            settingsRepository.setSettings(universe, new CharacterSettings(
                    settings.minPrimaryAttributeValue(),
                    settings.maxPrimaryAttributeValue(),
                    settings.maxPrimaryAttributeSum(),
                    BinaryExpressionTree.from(settings.tierFormula(), Set.of()),
                    BinaryExpressionTree.from(settings.talentPointFormula(), Set.of()),
                    settings.inventorySizes()
            ));
        } catch (IllegalFormulaException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("currency")
    @UniverseRead
    @Operation(summary = "Get the settings", operationId = "getCurrencySettings")
    public CurrencySettings getCurrencySettings(@PathVariable ObjectId universe) {
        return settingsRepository.getSettings(universe, CurrencySettings.class);
    }

    @PutMapping("currency")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the settings", operationId = "updateCurrencySettings")
    public void updateCurrencySettings(@PathVariable ObjectId universe, @Valid @RequestBody CurrencySettings settings) {
        settingsRepository.setSettings(universe, settings);
    }

    @GetMapping("item")
    @UniverseRead
    @Operation(summary = "Get the settings", operationId = "getItemSettings")
    public ItemSettings getItemSettings(@PathVariable ObjectId universe) {
        return settingsRepository.getSettings(universe, ItemSettings.class);
    }

    @PutMapping("item")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the settings", operationId = "updateItemSettings")
    public void updateItemSettings(@PathVariable ObjectId universe, @Valid @RequestBody ItemSettings settings) {
        settingsRepository.setSettings(universe, settings);
    }

    @GetMapping("equipment")
    @UniverseRead
    @Operation(summary = "Get the settings", operationId = "getEquipmentSettings")
    public EquipmentSettings getEquipmentSettings(@PathVariable ObjectId universe) {
        return settingsRepository.getSettings(universe, EquipmentSettings.class);
    }

    @PutMapping("equipment")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the settings", operationId = "updateEquipmentSettings")
    public void updateEquipmentSettings(@PathVariable ObjectId universe, @Valid @RequestBody EquipmentSettings settings) {
        settingsRepository.setSettings(universe, settings);
    }

    @GetMapping("character-sheet")
    @UniverseRead
    @Operation(summary = "Get the settings", operationId = "getCharacterSheetSettings")
    public CharacterSheetSettings getCharacterSheetSettings(@PathVariable ObjectId universe) {
        return settingsRepository.getSettings(universe, CharacterSheetSettings.class);
    }

    @PutMapping("character-sheet")
    @UniverseOwner
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the settings", operationId = "updateCharacterSheetSettings")
    public void updateCharacterSheetSettings(@PathVariable ObjectId universe, @Valid @RequestBody CharacterSheetSettings settings) {
        settingsRepository.setSettings(universe, settings);
    }
}
