package de.pnp.manager.component.inventory;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.interfaces.IItem;

public class ItemStack {

  private float amount;
  private final IItem item;

  public ItemStack(float amount, IItem item) {
    this.amount = amount;
    this.item = item;
  }

  public void addAmount(float amount) {
    setAmount(getAmount() + amount);
  }

  public void subtractAmount(float amount) {
    setAmount(getAmount() - amount);
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public float getAmount() {
    return amount;
  }

  public IItem getItem() {
    return item;
  }
}
