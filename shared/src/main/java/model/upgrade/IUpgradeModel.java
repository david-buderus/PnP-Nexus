package model.upgrade;

public interface IUpgradeModel {

    String getName();

    void setName(String name);

    String getTarget();

    void setTarget(String target);

    int getSlots();

    void setSlots(int slots);

    String getCost();

    void setCost(String cost);

    String getMana();

    void setMana(String mana);

    String getEffect();

    void setEffect(String effect);

    void setMaterials(String empty);

    String getMaterials();

    void addMaterial(String material);

    int getLevel();

    void setLevel(int level);

    String getRequirement();

    void setRequirement(String requirement);

    String getFullName();
}
