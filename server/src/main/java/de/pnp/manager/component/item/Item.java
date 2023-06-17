package de.pnp.manager.component.item;

import de.pnp.manager.annotations.ExportToTypescript;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.item.interfaces.IItem;
import de.pnp.manager.server.database.ItemRepository;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@ExportToTypescript
@Document(ItemRepository.REPOSITORY_NAME)
public class Item extends DatabaseObject implements IItem {

  @Indexed(unique = true)
  protected final String name;
  protected final String type;
  protected final String subtype;
  protected final String requirement;
  protected final String effect;
  protected final ERarity rarity;
  protected final int vendorPrice;
  protected final int tier;
  protected final String description;
  protected final String note;

  public Item(ObjectId id, String name, String type, String subtype, String requirement,
      String effect,
      ERarity rarity, int vendorPrice, int tier, String description, String note) {
    super(id);
    this.name = name;
    this.type = type;
    this.subtype = subtype;
    this.requirement = requirement;
    this.effect = effect;
    this.rarity = rarity;
    this.vendorPrice = vendorPrice;
    this.tier = tier;
    this.description = description;
    this.note = note;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getSubtype() {
    return subtype;
  }

  @Override
  public String getRequirement() {
    return requirement;
  }

  @Override
  public String getEffect() {
    return effect;
  }

  @Override
  public ERarity getRarity() {
    return rarity;
  }

  @Override
  public int getVendorPrice() {
    return vendorPrice;
  }

  @Override
  public int getTier() {
    return tier;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getNote() {
    return note;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || o.getClass() != getClass()) {
      return false;
    }
    Item item = (Item) o;

    return getVendorPrice() == item.getVendorPrice() && getTier() == item.getTier()
        && getName().equals(item.getName()) && getType().equals(item.getType())
        && getSubtype().equals(item.getSubtype()) && Objects.equals(getRequirement(),
        item.getRequirement()) && Objects.equals(getEffect(), item.getEffect())
        && getRarity() == item.getRarity() && Objects.equals(getDescription(),
        item.getDescription()) && Objects.equals(getNote(), item.getNote());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getType(), getSubtype(), getRequirement(), getEffect(),
        getRarity(), getVendorPrice(), getTier(), getDescription(), getNote());
  }
}