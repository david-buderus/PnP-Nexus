package de.pnp.manager.component;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Fabrication extends DatabaseObject {

  private final String profession;
  private final String requirement;
  private final String otherCircumstances;
  private final FabricationItem product;
  private final FabricationItem sideProduct;
  private final Collection<IFabricationEntry> materials;

  public Fabrication(ObjectId id, String profession, String requirement, String otherCircumstances,
      FabricationItem product, FabricationItem sideProduct,
      Collection<IFabricationEntry> materials) {
    super(id);
    this.profession = profession;
    this.requirement = requirement;
    this.otherCircumstances = otherCircumstances;
    this.product = Objects.requireNonNull(product);
    this.sideProduct = sideProduct;
    this.materials = materials;
  }


  public String getProfession() {
    return profession;
  }

  public String getRequirement() {
    return requirement;
  }

  public String getOtherCircumstances() {
    return otherCircumstances;
  }

  public FabricationItem getProduct() {
    return product;
  }

  public FabricationItem getSideProduct() {
    return sideProduct;
  }

  public Collection<IFabricationEntry> getMaterials() {
    return materials;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Fabrication that = (Fabrication) o;
    return Objects.equals(getProfession(), that.getProfession())
        && Objects.equals(getRequirement(), that.getRequirement())
        && Objects.equals(getOtherCircumstances(), that.getOtherCircumstances())
        && getProduct().equals(that.getProduct()) && Objects.equals(getSideProduct(),
        that.getSideProduct()) && Objects.equals(getMaterials(), that.getMaterials());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getProfession(), getRequirement(), getOtherCircumstances(), getProduct(),
        getSideProduct(), getMaterials());
  }

  public interface IFabricationEntry {

    float amount();
  }

  public record FabricationItem(float amount, @DBRef Item item) implements IFabricationEntry {

  }

  public record FabricationMaterial(float amount, @DBRef Material material) implements
      IFabricationEntry {

  }
}
