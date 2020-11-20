package ui.shop;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.item.Item;
import ui.View;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ShopView extends View {

    protected static Random rand = new Random();

    private final Collection<TableView<?>> tables = new ArrayList<>();
    private final BooleanProperty filtered = new SimpleBooleanProperty();

    protected void refresh() {
        for (TableView<?> table : tables) {
            table.refresh();
        }
    }

    protected TableBox createTable(Collection<?> list, String[] labels, String[] names, Class<?> clazz) {

        TableBox vBox = new TableBox(5);
        vBox.setPadding(new Insets(10, 20, 20, 20));

        SimpleListProperty<Object> filterList = new SimpleListProperty<>(FXCollections.observableArrayList(list));

        TableView<Object> table = new TableView<>();
        vBox.setTable(table);
        tables.add(table);
        table.itemsProperty().bindBidirectional(filterList);
        vBox.getChildren().add(table);

        ArrayList<FilterContainer> filterContainers = new ArrayList<>();

        HBox input = new HBox();
        vBox.getChildren().add(input);

        if (Item.class.isAssignableFrom(clazz)) {
            TableColumn<Object, Float> column = new TableColumn<>("Menge");
            column.setMaxWidth(400);
            column.setCellValueFactory(c -> ((Item) c.getValue()).amountProperty().asObject());
            column.setCellFactory(col -> new TableCell<>() {
                @Override
                public void updateItem(Float f, boolean empty) {
                    super.updateItem(f, empty);
                    String item = empty ? "" : String.valueOf(f);

                    BorderPane back = new BorderPane();

                    Text text = new Text(item);
                    back.setLeft(text);

                    if (this.getTableRow() != null) {
                        format(this.getTableRow().getItem(), text);
                    }

                    HBox box = new HBox(5);
                    Button plus = new Button("+");
                    plus.setPrefSize(25, 25);
                    plus.setMaxSize(25, 25);
                    plus.setOnMouseClicked(ev -> {
                        if (this.getTableRow() != null) {
                            ((Item) this.getTableRow().getItem()).addAmount(ctrlValues(ev));
                            refresh();
                        }
                    });
                    box.getChildren().add(plus);

                    Button minus = new Button("-");
                    minus.setPrefSize(25, 25);
                    minus.setMaxSize(25, 25);
                    minus.setOnMouseClicked(ev -> {
                        if (this.getTableRow() != null) {
                            ((Item) this.getTableRow().getItem()).addAmount(-ctrlValues(ev));
                            refresh();
                        }
                    });
                    box.getChildren().add(minus);

                    if (!empty) {
                        back.setRight(box);
                    }

                    this.setGraphic(back);
                }
            });
            table.getColumns().add(column);

            CheckBox filterBox = new CheckBox("Filtern");
            filtered.bindBidirectional(filterBox.selectedProperty());
            filterBox.setSelected(false);
            filterBox.setOnAction(ev -> update(filterContainers, list, filterList));
            filterBox.minWidthProperty().bind(column.widthProperty());
            filterBox.prefWidthProperty().bind(column.widthProperty());
            input.getChildren().add(filterBox);
        }

        for (int i = 0; i < labels.length; i++) {
            TableColumn<Object, Object> column = new TableColumn<>(labels[i]);
            column.setMaxWidth(400);
            column.setCellValueFactory(new PropertyValueFactory<>(names[i]));
            column.setCellFactory(col -> new TableCell<>() {
                @Override
                public void updateItem(Object object, boolean empty) {
                    super.updateItem(object, empty);
                    String item = empty ? "" : String.valueOf(object);

                    Text text = new Text(item);

                    if (this.getTableRow() != null) {
                        format(this.getTableRow().getItem(), text);
                    }

                    calculateSize(text);
                    this.widthProperty().addListener((ob, o, n) -> calculateSize(text));

                    this.setGraphic(text);
                }

                private void calculateSize(Text text) {
                    text.setWrappingWidth(0);
                    double width = text.getLayoutBounds().getWidth() > this.getTableColumn().getMaxWidth() ?
                            this.getWidth() - 25 : this.getWidth();
                    text.setWrappingWidth(width);
                    this.setMinHeight(text.getLayoutBounds().getHeight() + 10);
                    this.setPrefHeight(text.getLayoutBounds().getHeight() + 10);
                }
            });

            table.getColumns().add(column);

            Method getter = null;
            try {
                getter = new PropertyDescriptor(names[i], clazz).getReadMethod();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }

            TextField textField = new TextField();
            textField.minWidthProperty().bind(column.widthProperty());
            textField.prefWidthProperty().bind(column.widthProperty());
            textField.textProperty().addListener((ob, o, n) -> update(filterContainers, list, filterList));
            input.getChildren().add(textField);

            FilterContainer container = new FilterContainer();
            container.textField = textField;
            container.method = getter;
            filterContainers.add(container);
        }

        return vBox;
    }

    protected int ctrlValues(MouseEvent ev) {
        if (ev.getButton() != MouseButton.PRIMARY) {
            return 0;
        }

        if (ev.isShiftDown()) {
            return 100;
        } else if (ev.isControlDown()) {
            return 10;
        } else if (ev.isAltDown()) {
            return 5;
        } else {
            return 1;
        }
    }

    protected void format(Object object, Text text) {
    }

    protected void update(Collection<FilterContainer> filterContainers, Collection<?> fullList, ListProperty<Object> filteredList) {
        Stream<?> stream = fullList.stream();

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

        stream = stream.filter(o -> {
            if (filtered.get() && o instanceof Item) {
                return ((Item) o).getAmount() > 0;
            }
            return true;
        });

        filteredList.set(stream.collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    protected static class TableBox extends VBox {

        private TableView<?> table;

        public TableBox(int space) {
            super(space);
        }

        public TableView<?> getTable() {
            return table;
        }

        public void setTable(TableView<?> table) {
            this.table = table;
        }
    }

    protected static class FilterContainer {
        protected TextField textField;
        protected Method method;
    }
}
