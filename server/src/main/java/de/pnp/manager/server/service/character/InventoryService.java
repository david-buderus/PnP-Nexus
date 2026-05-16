package de.pnp.manager.server.service.character;

import de.pnp.manager.component.inventory.Inventory;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.Equipment;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Rest service to control inventories
 */
@RestController
@Validated
@RequestMapping("api/inventories")
public class InventoryService {

    @PostMapping("add")
    @Operation(summary = "Inserts the given stack into the inventory", operationId = "add")
    public Inventory add(@RequestBody @Validated InventoryAddRequest request) {
        ItemStack<? extends Item> itemStack = createItemStack(request.amount, request.item);
        if (!request.inventory.addItem(itemStack)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        return request.inventory;
    }

    @PostMapping("space")
    @Operation(summary = "Checks if the given stack can fit into the inventory", operationId = "hasSpaceFor")
    public boolean hasSpaceFor(@RequestBody @Validated InventoryAddRequest request) {
        ItemStack<? extends Item> itemStack = createItemStack(request.amount, request.item);
        return request.inventory.hasSpaceFor(itemStack);
    }

    @PostMapping("remove")
    @Operation(summary = "Removes the given stack from the inventory", operationId = "remove")
    public Inventory remove(@RequestBody @Validated InventoryRemoveRequest request) {
        request.inventory.removeItem(request.item, request.amount);
        return request.inventory;
    }

    private ItemStack<? extends Item> createItemStack(float stackSize, Item item) {
        if (item instanceof Weapon weapon) {
            return new WeaponEquipment(stackSize, weapon, 0);
        }
        if (item instanceof Shield shield) {
            return new ShieldEquipment(stackSize, shield, 0);
        }
        if (item instanceof Armor armor) {
            return new ArmorEquipment(stackSize, armor, 0);
        }
        if (item instanceof Jewellery jewellery) {
            return new Equipment<>(stackSize, jewellery);
        }
        return new ItemStack<>(stackSize, item);
    }

    /**
     * Request to add an item stack to an inventory
     */
    public record InventoryAddRequest(@NotNull Inventory inventory, @NotNull Item item, @Positive float amount) {
    }

    /**
     * Request to add an item stack to an inventory
     */
    public record InventoryRemoveRequest(@NotNull Inventory inventory, @NotNull Item item, @Positive float amount) {
    }
}
