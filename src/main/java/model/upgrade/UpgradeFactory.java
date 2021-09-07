package model.upgrade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.Currency;
import model.ItemList;
import model.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class UpgradeFactory {

    private static final Random rand = new Random();

    private String name;
    private String target;
    private int slots;
    private String[] requirements = new String[0];
    private Currency[] currencyList = new Currency[0];
    private String[] manaList = new String[0];
    private String[] effectList = new String[0];
    private ItemList[] materialsList = new ItemList[0];

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getMaxLevel() {
        return effectList.length;
    }

    public void setMaxLevel(int maxLevel) {
        if (maxLevel < getMaxLevel()) {
            return;
        }

        String[] regList = new String[maxLevel];
        System.arraycopy(requirements, 0, regList, 0, requirements.length);
        requirements = regList;

        Currency[] curList = new Currency[maxLevel];
        System.arraycopy(currencyList, 0, curList, 0, currencyList.length);
        currencyList = curList;

        String[] manList = new String[maxLevel];
        System.arraycopy(manaList, 0, manList, 0, manaList.length);
        manaList = manList;

        String[] effList = new String[maxLevel];
        System.arraycopy(effectList, 0, effList, 0, effectList.length);
        effectList = effList;

        ItemList[] matList = new ItemList[maxLevel];
        System.arraycopy(materialsList, 0, matList, 0, materialsList.length);
        materialsList = matList;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public void setCurrency(int level, Currency currency) {
        this.currencyList[level - 1] = currency;
    }

    public Currency getCurrency(int level) {
        return currencyList[level - 1];
    }

    public Currency getFullCost(int level) {
        Currency value = new Currency(0);

        for (int i = 1; i <= level; i++) {
            value.add(getCurrency(level));
        }

        return value;
    }

    public void setMana(int level, String mana) {
        this.manaList[level - 1] = mana;
    }

    public String getMana(int level) {
        return manaList[level - 1];
    }

    public void setEffect(int level, String effect) {
        this.effectList[level - 1] = effect;
    }

    public String getEffect(int level) {
        return effectList[level - 1];
    }

    public void setMaterials(int level, ItemList materials) {
        this.materialsList[level - 1] = materials;
    }

    public ItemList getMaterials(int level) {
        return materialsList[level - 1];
    }

    @JsonIgnore
    public ItemList getMaterialList() {
        return this.getMaterialList(1, this.getMaxLevel());
    }

    @JsonIgnore
    public ItemList getMaterialList(int from, int to) {
        ItemList items = new ItemList();

        for (int l = from; l <= to; l++) {
            items.addAll(getMaterials(l));
        }

        return items;
    }

    @JsonIgnore
    public Upgrade getUpgrade() {
        Upgrade upgrade = new Upgrade();
        upgrade.setName(getName());
        upgrade.setTarget(getTarget());
        upgrade.setSlots(getSlots());
        int level = calculateLevel(1);
        upgrade.setLevel(level);
        upgrade.setEffect(getEffect(level));
        upgrade.setCost(getFullCost(level));
        return upgrade;
    }

    private int calculateLevel(int i) {
        if (getMaxLevel() < i) {
            return getMaxLevel();
        }
        if (rand.nextDouble() < 0.5) {
            return calculateLevel(i + 1);
        }
        return i;
    }

    @JsonIgnore
    public Collection<UpgradeModel> getModels() {
        ArrayList<UpgradeModel> list = new ArrayList<>();

        for (int i = 1; i <= this.getMaxLevel(); i++) {
            UpgradeModel model = new UpgradeModel();
            model.setName(this.getName());
            model.setTarget(this.getTarget());
            model.setSlots(this.getSlots());
            model.setCost(this.getCurrency(i).getCoinString());
            model.setMana(this.getMana(i));
            model.setEffect(this.getEffect(i));
            model.setLevel(i);
            model.setRequirement(this.getRequirement(i));

            for (Item item : getMaterials(i)) {
                model.addMaterial(item != null ? item.getPrettyAmount() + " " + item.getName() : "");
            }

            list.add(model);
        }

        return list;
    }

    public String getRequirement(int level) {
        return requirements[level - 1];
    }

    public void setRequirement(int level, String requirement) {
        this.requirements[level - 1] = requirement;
    }

}
