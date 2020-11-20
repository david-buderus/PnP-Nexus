package model;

import manager.Utility;
import model.item.Item;

import java.util.Objects;
import java.util.stream.Collectors;

public class Fabrication {

    protected Item product = new Item();
    protected String profession = "";
    protected String requirement = "";
    protected String otherCircumstances = "";
    private int productAmount = 1;
    private Item sideProduct = new Item();
    private int sideProductAmount = 1;
    private ItemList materials = new ItemList();

    public Item getProduct() {
        return product;
    }

    public void setProduct(Item product) {
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

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public Item getSideProduct() {
        return sideProduct;
    }

    public void setSideProduct(Item sideProduct) {
        this.sideProduct = sideProduct;
    }

    public int getSideProductAmount() {
        return sideProductAmount;
    }

    public void setSideProductAmount(int sideProductAmount) {
        this.sideProductAmount = sideProductAmount;
    }

    public String getProductName() {
        return Objects.requireNonNullElse(product.getName(), "");
    }

    public void setProductName(String itemName) {
        product = Utility.getItem(itemName);
    }

    public String getSideProductName() {
        return Objects.requireNonNullElse(sideProduct.getName(), "");
    }

    public void setSideProductName(String itemName) {
        sideProduct = Utility.getItem(itemName);
    }

    public ItemList getMaterials() {
        return materials;
    }

    public void setMaterials(ItemList materials) {
        this.materials = materials;
    }

    public String getMaterialsAsString() {
        return materials.stream().map(item -> item.getPrettyAmount() + " " + item.getName()).collect(Collectors.joining("\n"));
    }
}
