package de.pnp.manager.component;

import de.pnp.manager.component.item.Item;
import java.util.Collection;
import java.util.Collections;

public class Fabrication {

  private final String profession;
  private final String requirement;
  private final String otherCircumstances;
  private final FabricationItem product;
  private final FabricationItem sideProduct;
  private final Collection<FabricationItem> materials;

  public Fabrication(String profession, String requirement, String otherCircumstances,
      FabricationItem product, FabricationItem sideProduct, Collection<FabricationItem> materials) {
    this.profession = profession;
    this.requirement = requirement;
    this.otherCircumstances = otherCircumstances;
    this.product = product;
    this.sideProduct = sideProduct;
    this.materials = Collections.unmodifiableCollection(materials);
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

  public Collection<FabricationItem> getMaterials() {
    return materials;
  }

  public record FabricationItem(float amount, Item item) {

  }
}
