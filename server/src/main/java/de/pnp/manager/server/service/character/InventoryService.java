package de.pnp.manager.server.service.character;

import de.pnp.manager.component.inventory.Inventory;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.Item;
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

    @PostMapping("add/item")
    @Operation(summary = "Inserts the given item into the inventory", operationId = "addItemToInventory")
    public Inventory add(@RequestBody @Validated InventoryItemAddRequest request) {
        ItemStack<? extends Item> itemStack = ItemStack.from(request.item, request.amount);
        if (!request.inventory.addItem(itemStack)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        return request.inventory;
    }

    @PostMapping("space/item")
    @Operation(summary = "Checks if the given item can fit into the inventory", operationId = "hasSpaceForInInventory")
    public boolean hasSpaceFor(@RequestBody @Validated InventoryItemAddRequest request) {
        ItemStack<? extends Item> itemStack = ItemStack.from(request.item, request.amount);
        return request.inventory.hasSpaceFor(itemStack);
    }

    @PostMapping("add/stack")
    @Operation(summary = "Inserts the given stack into the inventory", operationId = "addItemStackToInventory")
    public Inventory addStack(@RequestBody @Validated InventoryItemStackRequest request) {
        if (!request.inventory.addItem(request.itemStack)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        return request.inventory;
    }

    @PostMapping("space/stack")
    @Operation(summary = "Checks if the given stack can fit into the inventory", operationId = "hasSpaceForStackInInventory")
    public boolean hasSpaceForStack(@RequestBody @Validated InventoryItemStackRequest request) {
        return request.inventory.hasSpaceFor(request.itemStack);
    }

    @PostMapping("remove")
    @Operation(summary = "Removes the given stack from the inventory", operationId = "removeItemFromInventory")
    public Inventory remove(@RequestBody @Validated InventoryItemStackRequest request) {
        request.inventory.removeItem(request.itemStack);
        return request.inventory;
    }

    /**
     * Request to add an item to an inventory
     */
    public record InventoryItemAddRequest(@NotNull Inventory inventory, @NotNull Item item, @Positive float amount) {
    }


    /**
     * Request to add an item stack to an inventory
     */
    public record InventoryItemStackRequest(@NotNull Inventory inventory,
                                            @NotNull ItemStack<? extends Item> itemStack) {
    }
}
