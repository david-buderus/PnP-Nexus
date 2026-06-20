package de.pnp.manager.server.service.item;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.upgrade.Upgrade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ResourceBundle;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Rest service to upgrade equipment
 */
@RestController
@Validated
@RequestMapping("api/upgrade-item")
public class UpgradeItemService {


    @PostMapping("add")
    @Operation(summary = "Upgrades the given equipment", operationId = "addUpgradeToItem")
    public <I extends ItemStack<? extends Item>> I upgrade(@RequestBody @Valid UpgradeRequest<I> request) {
        ResourceBundle bundle = ResourceBundle.getBundle("upgrade", LocaleContextHolder.getLocale());
        I item = request.item;
        Upgrade upgrade = request.upgrade;
        if (!upgrade.getRestriction().applicableOn(item.getItem())) {
            throw new ResponseStatusException(BAD_REQUEST, bundle.getString("not_applicable"));
        }
        if (item.getRemainingUpgradeSlots() < upgrade.getSlots()) {
            throw new ResponseStatusException(BAD_REQUEST, bundle.getString("not_enough_slots"));
        }
        item.addUpgrade(upgrade);
        return item;
    }

    @PostMapping("remove")
    @Operation(summary = "Remove an upgrades from the given equipment", operationId = "removeUpgradeFromItem")
    public <I extends ItemStack<? extends Item>> I remove(@RequestBody @Valid UpgradeRemovalRequest<I> request) {
        ResourceBundle bundle = ResourceBundle.getBundle("upgrade", LocaleContextHolder.getLocale());
        I equipment = request.item;
        equipment.removeUpgrade(request.upgrade);
        if (equipment.getRemainingUpgradeSlots() < 0) {
            throw new ResponseStatusException(BAD_REQUEST, bundle.getString("not_enough_slots"));
        }
        return equipment;
    }

    /**
     * Request to upgrade an equipment.
     */
    public record UpgradeRequest<I extends ItemStack<? extends Item>>(
            @NotNull I item,
            @NotNull Upgrade upgrade) {

    }

    /**
     * Request to upgrade an equipment.
     */
    public record UpgradeRemovalRequest<I extends ItemStack<? extends Item>>(
            @NotNull I item,
            @NotNull Upgrade upgrade) {

    }
}
