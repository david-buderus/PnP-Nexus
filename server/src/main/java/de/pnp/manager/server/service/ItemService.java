package de.pnp.manager.server.service;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.database.ItemRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("{universe}")
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("item/{name}")
    @Operation(summary = "Get an item", operationId = "getItem")
    public Item item(@PathVariable String universe, @PathVariable String name) {
        return itemRepository.get(universe, name).orElse(null);
    }

    @GetMapping("a/{name}")
    @Operation(summary = "Get an item", operationId = "getItem")
    public Item a(@PathVariable String universe, @PathVariable String name) {
        return itemRepository.get(universe, name).orElse(null);
    }

    @GetMapping("itemstack/{name}")
    public ItemStack<Item> itemStack(@PathVariable String universe, @PathVariable String name) {
        return new ItemStack<>(10, item(universe, name));
    }
}
