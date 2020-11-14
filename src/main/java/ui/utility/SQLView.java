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
import manager.Utility;
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
        super("Items", parent);

        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        String[] labels = new String[]{"Name", "Typ", "Subtyp", "Tier", "Seltenheit", "Preis", "Effekt"};
        String[] names = new String[]{"name", "typ", "subTyp", "tier", "rarity", "cost", "effect"};

        root.getTabs().add(createTab("Items", labels, names, Utility.itemList, Item.class));

        labels = new String[]{"Name", "Typ", "Initiative", "Würfel/Belastung", "Schaden/Schutz", "Treffer", "Tier", "Seltenheit", "Preis", "Effekt", "Slots", "Voraussetzung"};
        names = new String[]{"name", "subTyp", "initiative", "dice", "damage", "hit", "tier", "rarity", "cost", "effect", "slots", "requirement"};

        root.getTabs().add(createTab("Waffen", labels, names, Utility.weaponList, Weapon.class));

        labels = new String[]{"Name", "Typ", "Schutz", "Belastung", "Tier", "Seltenheit", "Preis", "Effekt", "Slots", "Voraussetzung"};
        names = new String[]{"name", "subTyp", "protection", "weight", "tier", "rarity", "cost", "effect", "slots", "requirement"};

        root.getTabs().add(createTab("Rüstungen", labels, names, Utility.armorList, Armor.class));

        labels = new String[]{"Name", "Typ", "Material", "Edelstein", "Tier", "Seltenheit", "Preis", "Effekt", "Slots", "Voraussetzung"};
        names = new String[]{"name", "subTyp", "material", "gem", "tier", "rarity", "cost", "effect", "slots", "requirement"};

        root.getTabs().add(createTab("Schmuck", labels, names, Utility.jewelleryList, Jewellery.class));

        labels = new String[]{"Name", "Effekt", "Typ", "Kosten", "Zauberzeit", "Tier"};
        names = new String[]{"name", "effect", "typ", "cost", "castTime", "tier"};

        root.getTabs().add(createTab("Zauber", labels, names, Utility.spellList, Spell.class));

        labels = new String[]{"Name", "Stufe", "Ziel", "Effekt", "Slots", "Kosten", "Mana", "Materials"};
        names = new String[]{"name", "level", "target", "effect", "slots", "cost", "mana", "materials"};

        root.getTabs().add(createTab("Verbesserungen", labels, names, Utility.upgradeModelList, UpgradeModel.class));


        this.setContent(root);
    }

    private Tab createTab(String text, String[] labels, String[] names, ListProperty<?> utility, Class<?> itemClass){
        Tab tab = new Tab();
        tab.setText(text);

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

        for (int i = 0; i < labels.length; i++){
            TableColumn<Object, Object> column = new TableColumn<>(labels[i]);
            column.setMaxWidth(400);
            column.setCellValueFactory(new PropertyValueFactory<>(names[i]));
            column.setCellFactory(col -> new TableCell<>(){
                @Override
                public void updateItem(Object object, boolean empty) {
                    super.updateItem(object, empty);
                    String item = empty ? "" : String.valueOf(object);

                    Text text = new Text(item);
                    text.setStyle(" -fx-text-wrap: true;");

                    calculateSize(text);
                    this.widthProperty().addListener((ob, o ,n) -> calculateSize(text));

                    this.setGraphic(text);
                }

                private void calculateSize(Text text){
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

        tab.selectedProperty().addListener((ob, o ,n) -> getStage().sizeToScene());

        return tab;
    }

    private void update(Collection<?> utility, ListProperty<Object> list, Collection<FilterContainer> filterContainers){
        Stream<?> stream = utility.stream();

        for (FilterContainer container : filterContainers){
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

    private static class FilterContainer{
        private TextField textField;
        private Method method;
    }
}
