package model.item;

public interface IJewellery extends IEquipment {

    @Override
    IJewellery copy();

    String getGem();

    void setGem(String gem);
}
