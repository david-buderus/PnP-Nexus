package de.pnp.manager.model;

import de.pnp.manager.model.item.IItem;

public interface IFabrication {

    IItem getProduct();

    void setProduct(IItem product);

    String getProfession();

    void setProfession(String profession);

    String getRequirement();

    void setRequirement(String requirement);

    String getOtherCircumstances();

    void setOtherCircumstances(String otherCircumstances);

    int getProductAmount();

    void setProductAmount(int productAmount);

    IItem getSideProduct();

    void setSideProduct(IItem sideProduct);

    int getSideProductAmount();

    void setSideProductAmount(int sideProductAmount);

    IItemList getMaterials();

    void setMaterials(IItemList materials);
}
