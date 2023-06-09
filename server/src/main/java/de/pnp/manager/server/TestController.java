package de.pnp.manager.server;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/item")
  public Item item() {
    return new Item("Bier", "Nahrung", "Trinken", "", "", ERarity.COMMON, 10, 1, "", "");
  }

  @GetMapping("/itemstack")
  public ItemStack<?> itemStack() {
    return new ItemStack<>(10, item());
  }
}
