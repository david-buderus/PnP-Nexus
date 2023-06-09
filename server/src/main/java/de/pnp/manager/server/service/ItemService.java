package de.pnp.manager.server.service;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.database.ItemRepository;
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
  public Item item(@PathVariable String universe, @PathVariable String name) {
    return itemRepository.get(universe, name).orElse(null);
  }

  @GetMapping("itemstack/{name}")
  public ItemStack<?> itemStack(@PathVariable String universe, @PathVariable String name) {
    return new ItemStack<>(10, item(universe, name));
  }
}
