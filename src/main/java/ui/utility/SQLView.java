package ui.utility;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import manager.Database;
import manager.LanguageUtility;
import model.Spell;
import model.item.Armor;
import model.item.Item;
import model.item.Jewellery;
import model.item.Weapon;
import model.upgrade.UpgradeModel;
import ui.IView;
import ui.ViewPart;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SQLView extends ViewPart {

    public SQLView(IView parent) {
        super("sql.title", parent);

        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        String[] labels = new String[]{"column.name", "column.type", "column.item.subtype", "column.tier", "column.item.rarity", "column.item.price", "column.effect"};
        String[] names = new String[]{"name", "type", "subtype", "tier", "rarity", "currency", "effect"};

        root.getTabs().add(createTab("sql.tab.items", labels, names, Database.itemList, Item.class));

        labels = new String[]{"column.name", "column.type", "column.weapon.initiative", "column.dice_weight", "column.damage_protection", "column.weapon.hit", "column.tier", "column.item.rarity", "column.item.price", "column.effect", "column.equipment.slots", "column.equipment.requirement"};
        names = new String[]{"name", "subtype", "initiative", "dice", "damage", "hit", "tier", "rarity", "currency", "effect", "slots", "requirement"};

        root.getTabs().add(createTab("sql.tab.weapons", labels, names, Database.weaponList, Weapon.class));

        labels = new String[]{"column.name", "column.type", "Schutz", "Belastung", "column.tier", "column.item.rarity", "column.item.price", "column.effect", "column.equipment.slots", "column.equipment.requirement"};
        names = new String[]{"name", "subtype", "protection", "weight", "tier", "rarity", "currency", "effect", "slots", "requirement"};

        root.getTabs().add(createTab("sql.tab.armor", labels, names, Database.armorList, Armor.class));

        labels = new String[]{"column.name", "column.type", "Material", "Edelstein", "column.tier", "column.item.rarity", "column.item.price", "column.effect", "column.equipment.slots", "column.equipment.requirement"};
        names = new String[]{"name", "subtype", "material", "gem", "tier", "rarity", "currency", "effect", "slots", "requirement"};

        root.getTabs().add(createTab("sql.tab.jewellery", labels, names, Database.jewelleryList, Jewellery.class));

        labels = new String[]{"column.name", "column.effect", "column.type", "column.spell.cost", "column.spell.castTime", "column.tier"};
        names = new String[]{"name", "effect", "type", "cost", "castTime", "tier"};

        root.getTabs().add(createTab("sql.tab.spell", labels, names, Database.spellList, Spell.class));

        labels = new String[]{"column.name", "Stufe", "Ziel", "column.effect", "column.equipment.slots", "column.item.price", "column.upgrade.mana", "column.upgrade.materials"};
        names = new String[]{"name", "level", "target", "effect", "slots", "cost", "mana", "materials"};

        root.getTabs().add(createTab("sql.tab.upgrades", labels, names, Database.upgradeModelList, UpgradeModel.class));


        this.setContent(root);
    }

    private Tab createTab(String key, String[] labels, String[] names, ListProperty<?> utility, Class<?> itemClass) {
        Tab tab = new Tab();
        tab.textProperty().bind(LanguageUtility.getMessageProperty(key));

        ListProperty<Object> list = new SimpleListProperty<>();
        utility.addListener((ob, o, n) -> list.set(n.stream().collect(Collectors.toCollection(FXCollections::observableArrayList))));
        list.set(utility.stream().collect(Collectors.toCollection(FXCollections::observableArrayList)));

        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(10, 20, 20, 20));
        tab.setContent(vBox);

        TableView<Object> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.itemsProperty().bindBidirectional(list);
        vBox.getChildren().add(table);

        ArrayList<FilterContainer> filterContainers = new ArrayList<>();

        HBox input = new HBox();
        vBox.getChildren().add(input);

        for (int i = 0; i < labels.length; i++) {
            TableColumn<Object, Object> column = new TableColumn<>();
            column.textProperty().bind(LanguageUtility.getMessageProperty(labels[i]));
            column.setMaxWidth(400);
            column.setCellValueFactory(new PropertyValueFactory<>(names[i]));
            column.setCellFactory(col -> new TableCell<>() {
                @Override
                public void updateItem(Object object, boolean empty) {
                    super.updateItem(object, empty);
                    String item = empty ? "" : String.valueOf(object);

                    Text text = new Text(item);
                    text.setStyle(" -fx-text-wrap: true;");

                    calculateSize(text);
                    this.widthProperty().addListener((ob, o, n) -> calculateSize(text));

                    this.setGraphic(text);
                }

                private void calculateSize(Text text) {
                    text.setWrappingWidth(0);
                    double width = text.getLayoutBounds().getWidth() > this.getTableColumn().getMaxWidth() ?
                            this.getWidth() - 25 : this.getWidth();
                    text.setWrappingWidth(width);
                    this.setPrefHeight(text.getLayoutBounds().getHeight() + 10);
                }
            });

            table.getColumns().add(column);

            Method getter = null;
            try {
                getter = new PropertyDescriptor(names[i], itemClass).getReadMethod();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }

            TextField textField = new TextField();
            textField.minWidthProperty().bind(column.widthProperty());
            textField.prefWidthProperty().bind(column.widthProperty());
            textField.textProperty().addListener((ob, o, n) -> update(utility, list, filterContainers));
            input.getChildren().add(textField);

            FilterContainer container = new FilterContainer();
            container.textField = textField;
            container.method = getter;
            filterContainers.add(container);
        }

        tab.selectedProperty().addListener((ob, o, n) -> getStage().sizeToScene());

        return tab;
    }

    private void update(Collection<?> utility, ListProperty<Object> list, Collection<FilterContainer> filterContainers) {
        Stream<?> stream = utility.stream();

        for (FilterContainer container : filterContainers) {
            stream = stream.filter(x -> {
                try {
                    return (String.valueOf(container.method.invoke(x))).toLowerCase().contains(container.textField.getText().toLowerCase());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return false;
            });
        }

        list.set(stream.collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    private static class FilterContainer {
        private TextField textField;
        private Method method;
    }
}
