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

    float getProductAmount();

    void setProductAmount(float productAmount);

    IItem getSideProduct();

    void setSideProduct(IItem sideProduct);

    float getSideProductAmount();

    void setSideProductAmount(float sideProductAmount);

    IItemList getMaterials();

    void setMaterials(IItemList materials);
}
