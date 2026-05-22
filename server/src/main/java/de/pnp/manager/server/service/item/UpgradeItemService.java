package de.pnp.manager.server.service.item;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.upgrade.Upgrade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Rest service to upgrade equipment
 */
@RestController
@Validated
@RequestMapping("api/upgrade-item")
public class UpgradeItemService {


    @PostMapping("add")
    @Operation(summary = "Upgrades the given equipment", operationId = "addUpgrade")
    public <I extends EquipableItem, E extends ItemStack<? extends I>> E upgrade(@RequestBody @Valid UpgradeRequest<I, E> request) {
        E equipment = request.equipment;
        Upgrade upgrade = request.upgrade;
        if (!upgrade.getRestriction().applicableOn(equipment.getItem())) {
            throw new ResponseStatusException(BAD_REQUEST, "The upgrade is not applicable to the item.");
        }
        if (equipment.getRemainingUpgradeSlots() < upgrade.getSlots()) {
            throw new ResponseStatusException(BAD_REQUEST, "The equipment has not enough slots to hold the upgrade.");
        }
        equipment.addUpgrade(upgrade);
        return equipment;
    }

    @PostMapping("remove")
    @Operation(summary = "Remove an upgrades from the given equipment", operationId = "removeUpgrade")
    public <I extends EquipableItem, E extends ItemStack<? extends I>> E remove(@RequestBody @Valid UpgradeRemovalRequest<I, E> request) {
        E equipment = request.equipment;
        equipment.removeUpgrade(request.upgrade);
        return equipment;
    }

    /**
     * Request to upgrade an equipment.
     */
    public record UpgradeRequest<I extends EquipableItem, E extends ItemStack<? extends I>>(
            @NotNull E equipment,
            @NotNull Upgrade upgrade) {

    }

    /**
     * Request to upgrade an equipment.
     */
    public record UpgradeRemovalRequest<I extends EquipableItem, E extends ItemStack<? extends I>>(
            @NotNull E equipment,
            @NotNull Upgrade upgrade) {

    }
}
