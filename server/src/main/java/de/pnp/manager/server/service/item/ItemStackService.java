package de.pnp.manager.server.service.item;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.JewelleryEquipment;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.Jewellery;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest service to create {@link ItemStack}
 */
@RestController
@Validated
@RequestMapping("api/itemstack")
public class ItemStackService {

    @PostMapping
    @Operation(summary = "Creates the matching item stack for the given item", operationId = "create")
    public ItemStack<? extends Item> create(@RequestBody @Valid ItemStackRequest<?> request) {
        return ItemStack.from(request.item, request.stackSize);
    }

    @PostMapping("weapon")
    @Operation(summary = "Creates the matching item stack for the given item", operationId = "createWeapon")
    public WeaponEquipment createWeapon(@RequestBody @Valid ItemStackRequest<Weapon> request) {
        return new WeaponEquipment(request.stackSize, request.item, 0);
    }

    @PostMapping("shield")
    @Operation(summary = "Creates the matching item stack for the given item", operationId = "createShield")
    public ShieldEquipment createShield(@RequestBody @Valid ItemStackRequest<Shield> request) {
        return new ShieldEquipment(request.stackSize, request.item, 0);
    }

    @PostMapping("armor")
    @Operation(summary = "Creates the matching item stack for the given item", operationId = "createArmor")
    public ArmorEquipment createArmor(@RequestBody @Valid ItemStackRequest<Armor> request) {
        return new ArmorEquipment(request.stackSize, request.item, 0);
    }

    @PostMapping("jewellery")
    @Operation(summary = "Creates the matching item stack for the given item", operationId = "createJewellery")
    public JewelleryEquipment createJewellery(@RequestBody @Valid ItemStackRequest<Jewellery> request) {
        return new JewelleryEquipment(request.stackSize, request.item);
    }

    /**
     * Request to create an {@link ItemStack}.
     */
    public record ItemStackRequest<I extends Item>(@Positive float stackSize, @NotNull I item) {

    }
}
