package ui.utility;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import manager.Database;
import manager.LanguageUtility;
import model.Spell;
import model.item.*;
import model.upgrade.UpgradeModel;
import ui.IView;
import ui.ViewPart;
import ui.part.WrappingTableCell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SQLView extends ViewPart {

    public SQLView(IView parent) {
        super("sql.title", parent);

        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        List<ColumnDefinition<Item>> itemColumns = Arrays.asList(
                new ColumnDefinition<>("column.name", Item::getName),
                new ColumnDefinition<>("column.type", Item::getTier),
                new ColumnDefinition<>("column.item.subtype", Item::getSubtype),
                new ColumnDefinition<>("column.tier", Item::getTier),
                new ColumnDefinition<>("column.item.rarity", Item::getRarity),
                new ColumnDefinition<>("column.item.price", Item::getCurrency),
                new ColumnDefinition<>("column.item.effect", Item::getEffect)
        );
        root.getTabs().add(createTab("sql.tab.items", itemColumns, Database.itemList));

        List<ColumnDefinition<Weapon>> weaponColumns = Arrays.asList(
                new ColumnDefinition<>("column.name", Item::getName),
                new ColumnDefinition<>("column.type", Item::getSubtype),
                new ColumnDefinition<>("column.weapon.initiative", Weapon::getInitiative),
                new ColumnDefinition<>("column.dice_weight", Weapon::getDice),
                new ColumnDefinition<>("column.damage_protection", Weapon::getDamage),
                new ColumnDefinition<>("column.weapon.hit", Weapon::getHit),
                new ColumnDefinition<>("column.tier", Item::getTier),
                new ColumnDefinition<>("column.item.rarity", Item::getRarity),
                new ColumnDefinition<>("column.item.price", Item::getCurrency),
                new ColumnDefinition<>("column.effect", Item::getEffect),
                new ColumnDefinition<>("column.equipment.slots", Equipment::getUpgradeSlots),
                new ColumnDefinition<>("column.equipment.requirement", Equipment::getRequirement)
        );
        root.getTabs().add(createTab("sql.tab.weapons", weaponColumns, Database.weaponList));

        List<ColumnDefinition<Armor>> armorColumns = Arrays.asList(
                new ColumnDefinition<>("column.name", Item::getName),
                new ColumnDefinition<>("column.type", Item::getSubtype),
                new ColumnDefinition<>("column.armor.protection", Armor::getProtection),
                new ColumnDefinition<>("column.armor.weight", Armor::getWeight),
                new ColumnDefinition<>("column.tier", Item::getTier),
                new ColumnDefinition<>("column.item.rarity", Item::getRarity),
                new ColumnDefinition<>("column.item.price", Item::getCurrency),
                new ColumnDefinition<>("column.effect", Item::getEffect),
                new ColumnDefinition<>("column.equipment.slots", Equipment::getUpgradeSlots),
                new ColumnDefinition<>("column.equipment.requirement", Equipment::getRequirement)
        );
        root.getTabs().add(createTab("sql.tab.armor", armorColumns, Database.armorList));

        List<ColumnDefinition<Jewellery>> jewelleryColumns = Arrays.asList(
                new ColumnDefinition<>("column.name", Item::getName),
                new ColumnDefinition<>("column.type", Item::getSubtype),
                new ColumnDefinition<>("column.equipment.material", Equipment::getMaterial),
                new ColumnDefinition<>("column.jewellery.gem", Jewellery::getGem),
                new ColumnDefinition<>("column.tier", Item::getTier),
                new ColumnDefinition<>("column.item.rarity", Item::getRarity),
                new ColumnDefinition<>("column.item.price", Item::getCurrency),
                new ColumnDefinition<>("column.effect", Item::getEffect),
                new ColumnDefinition<>("column.equipment.slots", Equipment::getUpgradeSlots),
                new ColumnDefinition<>("column.equipment.requirement", Equipment::getRequirement)
        );
        root.getTabs().add(createTab("sql.tab.jewellery", jewelleryColumns, Database.jewelleryList));

        List<ColumnDefinition<Spell>> spellColumns = Arrays.asList(
                new ColumnDefinition<>("column.name", Spell::getName),
                new ColumnDefinition<>("column.effect", Spell::getEffect),
                new ColumnDefinition<>("column.type", Spell::getType),
                new ColumnDefinition<>("column.spell.cost", Spell::getCost),
                new ColumnDefinition<>("column.spell.castTime", Spell::getCastTime),
                new ColumnDefinition<>("column.tier", Spell::getTier)
        );
        root.getTabs().add(createTab("sql.tab.spell", spellColumns, Database.spellList));

        List<ColumnDefinition<UpgradeModel>> upgradeColumns = Arrays.asList(
                new ColumnDefinition<>("column.name", UpgradeModel::getName),
                new ColumnDefinition<>("column.upgrade.level", UpgradeModel::getLevel),
                new ColumnDefinition<>("column.target", UpgradeModel::getTarget),
                new ColumnDefinition<>("column.effect", UpgradeModel::getEffect),
                new ColumnDefinition<>("column.equipment.slots", UpgradeModel::getSlots),
                new ColumnDefinition<>("column.item.price", UpgradeModel::getCost),
                new ColumnDefinition<>("column.upgrade.mana", UpgradeModel::getMana),
                new ColumnDefinition<>("column.upgrade.materials", UpgradeModel::getMaterials)
        );
        root.getTabs().add(createTab("sql.tab.upgrades", upgradeColumns, Database.upgradeModelList));


        this.setContent(root);
    }

    private <Obj> Tab createTab(String key, List<ColumnDefinition<Obj>> columnDefinitions, ListProperty<Obj> utility) {
        Tab tab = new Tab();
        tab.textProperty().bind(LanguageUtility.getMessageProperty(key));

        ListProperty<Obj> list = new SimpleListProperty<>();
        utility.addListener((ob, o, n) -> list.set(n.stream().collect(Collectors.toCollection(FXCollections::observableArrayList))));
        list.set(utility.stream().collect(Collectors.toCollection(FXCollections::observableArrayList)));

        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(10, 20, 20, 20));
        tab.setContent(vBox);

        TableView<Obj> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.itemsProperty().bindBidirectional(list);
        table.itemsProperty().addListener((ob, o, n) -> table.layout());
        vBox.getChildren().add(table);

        ArrayList<FilterContainer<Obj>> filterContainers = new ArrayList<>();

        HBox input = new HBox();
        vBox.getChildren().add(input);

        for (ColumnDefinition<Obj> columnDefinition : columnDefinitions) {

            TableColumn<Obj, String> column = new TableColumn<>();
            column.textProperty().bind(LanguageUtility.getMessageProperty(columnDefinition.key));
            column.setMaxWidth(400);
            column.setCellValueFactory(cell -> new ReadOnlyStringWrapper(String.valueOf(columnDefinition.getter.apply(cell.getValue()))));
            column.setCellFactory(col -> new WrappingTableCell<>());
            table.getColumns().add(column);

            TextField textField = new TextField();
            textField.minWidthProperty().bind(column.widthProperty());
            textField.prefWidthProperty().bind(column.widthProperty());
            textField.textProperty().addListener((ob, o, n) -> update(utility, list, filterContainers));
            input.getChildren().add(textField);

            filterContainers.add(new FilterContainer<>(textField, columnDefinition.getter));
        }

        tab.selectedProperty().addListener((ob, o, n) -> getStage().sizeToScene());

        return tab;
    }

    private <Obj> void update(Collection<Obj> utility, ListProperty<Obj> list, Collection<FilterContainer<Obj>> filterContainers) {
        Stream<Obj> stream = utility.stream();

        for (FilterContainer<Obj> container : filterContainers) {
            stream = stream.filter(x -> String.valueOf(container.getter.apply(x)).toLowerCase().contains(container.textField.getText().toLowerCase()));
        }

        list.set(stream.collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    private static class ColumnDefinition<Obj> {
        private final String key;
        private final Function<Obj, Object> getter;

        public ColumnDefinition(String key, Function<Obj, Object> getter) {
            this.key = key;
            this.getter = getter;
        }
    }

    private static class FilterContainer<Obj> {
        private final TextField textField;
        private final Function<Obj, Object> getter;

        public FilterContainer(TextField textField, Function<Obj, Object> getter) {
            this.textField = textField;
            this.getter = getter;
        }
    }
}
