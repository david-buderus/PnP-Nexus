package de.pnp.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.main.Database;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.item.Item;

import java.util.Objects;
import java.util.stream.Collectors;

public class Fabrication implements IFabrication {

    protected IItem product = Item.EMPTY_ITEM;
    protected String profession = "";
    protected String requirement = "";
    protected String otherCircumstances = "";
    private float productAmount = 1;
    private IItem sideProduct = Item.EMPTY_ITEM;
    private float sideProductAmount = 1;
    private IItemList materials = new ItemList();

    public IItem getProduct() {
        return product;
    }

    public void setProduct(IItem product) {
        this.product = product;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getOtherCircumstances() {
        return otherCircumstances;
    }

    public void setOtherCircumstances(String otherCircumstances) {
        this.otherCircumstances = otherCircumstances;
    }

    public float getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(float productAmount) {
        this.productAmount = productAmount;
    }

    public IItem getSideProduct() {
        return sideProduct;
    }

    public void setSideProduct(IItem sideProduct) {
        this.sideProduct = sideProduct;
    }

    public float getSideProductAmount() {
        return sideProductAmount;
    }

    public void setSideProductAmount(float sideProductAmount) {
        this.sideProductAmount = sideProductAmount;
    }

    @JsonIgnore
    public String getProductName() {
        return Objects.requireNonNullElse(product.getName(), "");
    }

    public void setProductName(String itemName) {
        product = Database.getItem(itemName);
    }

    @JsonIgnore
    public String getSideProductName() {
        return Objects.requireNonNullElse(sideProduct.getName(), "");
    }

    public void setSideProductName(String itemName) {
        sideProduct = Database.getItem(itemName);
    }

    public IItemList getMaterials() {
        return materials;
    }

    public void setMaterials(IItemList materials) {
        this.materials = materials;
    }

    @JsonIgnore
    public String getMaterialsAsString() {
        return materials.stream().map(item -> item.getPrettyAmount() + " " + item.getName()).collect(Collectors.joining("\n"));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Fabrication)) {
            return false;
        }

        Fabrication other = (Fabrication) obj;

        return getProduct().equals(other.getProduct()) && getProfession().equals(other.getProfession())
                && getRequirement().equals(other.getRequirement()) && getOtherCircumstances().equals(other.getOtherCircumstances())
                && getProductAmount() == other.getProductAmount() && getSideProduct().equals(other.getSideProduct())
                && getSideProductAmount() == other.getSideProductAmount() && getMaterials().equals(other.getMaterials());
    }
}
