package city.shop;

import city.Shop;
import city.Town;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import manager.Utility;
import model.ItemList;
import model.upgrade.UpgradeFactory;
import model.upgrade.UpgradeModel;
import model.item.Item;
import ui.shop.EnchanterView;
import ui.shop.ShopView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Enchanter extends Shop {

    private ItemList materials;
    private ItemList realMaterials;
    private ArrayList<UpgradeModel> upgrades;

    private ObservableMap<UpgradeModel, IntegerProperty> amount;
    private ListProperty<UpgradeModel> selected;
    private ListProperty<Item> difference;

    public Enchanter(Town town) {
        super(town, "Verzauberer");
        this.materials = new ItemList();
        this.realMaterials = new ItemList();
        this.amount = new SimpleMapProperty<>(FXCollections.observableHashMap());
        this.selected = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.difference = new SimpleListProperty<>(FXCollections.observableArrayList());

        this.upgrades = Utility.upgradeModelList.stream().filter(up -> up.getRequirement().contains("Verzauberung"))
                .collect(Collectors.toCollection(ArrayList::new));

        for(UpgradeModel model : upgrades){
            UpgradeFactory upgradeFactory = Utility.upgradeMap.get(model);
            for(Item material : upgradeFactory.getMaterialList()){
                material.setAmount(calculateAmount(material));
                materials.add(material);
            }
        }
        realMaterials.addAll(materials);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(this);
            System.out.println(s);
            Enchanter e = mapper.readValue(s, Enchanter.class);

            System.out.println(e);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    @JsonIgnore
    public ShopView getView() {
        return new EnchanterView(this);
    }

    @Override
    @JsonIgnore
    public double getSpawnChance() {
        switch (town.getTyp()){

            case bigTown:
                return 1;
            case town:
                return 0.8;
            case smallTown:
                return 0.4;
            case bigVillage:
                return 0.2;
            case village:
                return 0.1;
            case smallVillage:
                return 0;
        }
        return 0;
    }

    public void add(Collection<?> toAdd){
        for(Object object : toAdd) {
            UpgradeModel model = (UpgradeModel) object;
            if (selected.contains(model)) {
                this.amount.get(model).set(this.amount.get(model).get() + 1);
            } else {
                this.amount.put(model, new SimpleIntegerProperty(1));
                this.selected.add(model);
            }
        }
        updateDifference();
    }

    public void remove(UpgradeModel model){
        if(selected.contains(model)){
            if(this.amount.get(model).get() == 1){
                this.selected.remove(model);
                this.amount.remove(model);
            } else {
                this.amount.get(model).set(this.amount.get(model).get() - 1);
            }
            updateDifference();
        }
    }

    public void clear(){
        selected.clear();
        amount.clear();
        updateDifference();
    }

    public void buy(){
        materials.remove(calculateCosts());
        clear();
    }

    private void updateDifference() {
        ItemList cost = calculateCosts();
        realMaterials.clear();
        realMaterials.addAll(materials);
        realMaterials.remove(cost);
        difference.clear();
        difference.addAll(materials.difference(cost));
    }

    private ItemList calculateCosts() {
        ItemList cost = new ItemList();

        for(UpgradeModel model : selected){
            Collection<Item> items = Utility.upgradeMap.get(model).getMaterialList(model.getLevel(), model.getLevel());
            for(int i=0; i<amount.get(model).get(); i++){
                cost.addAll(items);
            }
        }
        return cost;
    }

    public ItemList getMaterials() {
        return materials;
    }

    @JsonIgnore
    public ItemList getRealMaterials() {
        return realMaterials;
    }

    @JsonIgnore
    public ArrayList<UpgradeModel> getUpgrades() {
        return upgrades;
    }

    @JsonIgnore
    public ObservableMap<UpgradeModel, IntegerProperty> getAmount() {
        return amount;
    }

    @JsonIgnore
    public ObservableList<UpgradeModel> getSelected() {
        return selected.get();
    }

    public ListProperty<UpgradeModel> selectedProperty() {
        return selected;
    }

    @JsonIgnore
    public ObservableList<Item> getDifference() {
        return difference.get();
    }

    public ListProperty<Item> differenceProperty() {
        return difference;
    }
}
