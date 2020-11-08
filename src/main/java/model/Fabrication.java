package model;

import manager.Utility;
import model.item.Item;

import java.util.Objects;

public class Fabrication {

    protected Item product = new Item();
    protected String profession = "";
    protected String requirement = "";
    protected String otherCircumstances = "";
    private int productAmount = 1;
    private Item sideProduct = new Item();
    private int sideProductAmount = 1;
    private int[] amountList = new int[10];
    private Item[] materialList = new Item[10];

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

    public int[] getAmountList() {
        return amountList;
    }

    public void setAmountList(int[] amountList) {
        this.amountList = amountList;
    }

    public Item[] getMaterialList() {
        return materialList;
    }

    public void setMaterialList(Item[] materialList) {
        this.materialList = materialList;
    }

    public String getProductName(){
        return Objects.requireNonNullElse(product.getName(), "");
    }

    public void setProductName(String itemName){
        product = Utility.getItem(itemName);
    }

    public String getSideProductName(){
        return Objects.requireNonNullElse(sideProduct.getName(), "");
    }

    public void setSideProductName(String itemName){
        sideProduct = Utility.getItem(itemName);
    }

    public String getMaterial1(){
        return Objects.requireNonNullElse(materialList[0].getName(), "");
    }

    public void setMaterial1(String itemName){
        materialList[0] = Utility.getItem(itemName);
    }

    public String getMaterial2(){
        return Objects.requireNonNullElse(materialList[1].getName(), "");
    }

    public void setMaterial2(String itemName){
        materialList[1] = Utility.getItem(itemName);
    }

    public String getMaterial3(){
        return Objects.requireNonNullElse(materialList[2].getName(), "");
    }

    public void setMaterial3(String itemName){
        materialList[2] = Utility.getItem(itemName);
    }

    public String getMaterial4(){
        return Objects.requireNonNullElse(materialList[3].getName(), "");
    }

    public void setMaterial4(String itemName){
        materialList[3] = Utility.getItem(itemName);
    }

    public String getMaterial5(){
        return Objects.requireNonNullElse(materialList[4].getName(), "");
    }

    public void setMaterial5(String itemName){
        materialList[4] = Utility.getItem(itemName);
    }

    public String getMaterial6(){
        return Objects.requireNonNullElse(materialList[5].getName(), "");
    }

    public void setMaterial6(String itemName){
        materialList[5] = Utility.getItem(itemName);
    }

    public String getMaterial7(){
        return Objects.requireNonNullElse(materialList[6].getName(), "");
    }

    public void setMaterial7(String itemName){
        materialList[6] = Utility.getItem(itemName);
    }

    public String getMaterial8(){
        return Objects.requireNonNullElse(materialList[7].getName(), "");
    }

    public void setMaterial8(String itemName){
        materialList[7] = Utility.getItem(itemName);
    }

    public String getMaterial9(){
        return Objects.requireNonNullElse(materialList[8].getName(), "");
    }

    public void setMaterial9(String itemName){
        materialList[8] = Utility.getItem(itemName);
    }

    public String getMaterial10(){
        return Objects.requireNonNullElse(materialList[9].getName(), "");
    }

    public void setMaterial10(String itemName){
        materialList[9] = Utility.getItem(itemName);
    }
}
