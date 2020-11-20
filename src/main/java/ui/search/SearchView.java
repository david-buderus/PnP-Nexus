package ui.search;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.item.Equipment;
import model.item.Item;
import ui.IView;
import ui.ViewPart;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SearchView<Typ> extends ViewPart {

    protected ListProperty<Typ> showingList;
    protected ListProperty<Typ> fullList;
    protected Class<Typ> typClass;
    private final ArrayList<FilterContainer> filterContainers;

    public SearchView(String title, IView parent, Class<Typ> typClass) {
        super(title, parent);
        this.typClass = typClass;
        this.showingList = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.fullList = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.fullList.addListener((ob, o, n) -> showingList.set(n));
        this.showingList.set(fullList);
        this.filterContainers = new ArrayList<>();
    }

    protected VBox createRoot(String[] labels, String[] names) {
        return createRoot(labels, names, new double[names.length]);
    }

    protected VBox createRoot(String[] labels, String[] names, double[] widths) {
        VBox root = new VBox(5);
        root.setPadding(new Insets(10, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        HBox titleLine = new HBox(10);
        titleLine.setAlignment(Pos.CENTER);

        root.getChildren().add(titleLine);

        TableView<Typ> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.itemsProperty().bindBidirectional(showingList);
        root.getChildren().add(table);

        HBox input = new HBox();
        root.getChildren().add(input);

        if (Item.class.isAssignableFrom(typClass)) {
            createCounter(table, input);
        }

        for (int i = 0; i < labels.length; i++) {
            createColumn(table, input, labels[i], names[i], widths[i]);
        }

        if (Equipment.class.isAssignableFrom(typClass)) {
            createUpgrade(table, input);
        }

        return root;
    }

    protected void filterTable() {
        Stream<Typ> stream = fullList.stream();

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

        showingList.set(stream.collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    private void createCounter(TableView<Typ> table, HBox input) {
        TableColumn<Typ, Float> column = new TableColumn<>("Menge");
        column.setMaxWidth(400);
        column.setCellValueFactory(c -> ((Item) c.getValue()).amountProperty().asObject());
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            public void updateItem(Float f, boolean empty) {
                super.updateItem(f, empty);
                String item = empty ? "" : String.valueOf(f);

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

        TextField textField = new TextField();
        textField.minWidthProperty().bind(column.widthProperty());
        textField.prefWidthProperty().bind(column.widthProperty());
        textField.textProperty().addListener((ob, o, n) -> filterTable());
        input.getChildren().add(textField);
    }

    private void createUpgrade(TableView<Typ> table, HBox input) {
        TableColumn<Typ, String> column = new TableColumn<>("Verbesserungen");
        column.setMaxWidth(400);
        column.setCellValueFactory(c -> new ReadOnlyStringWrapper(((Equipment) c.getValue()).upgradesAsString()));
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            public void updateItem(String string, boolean empty) {
                super.updateItem(string, empty);
                String item = empty ? "" : string;

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

        TextField textField = new TextField();
        textField.minWidthProperty().bind(column.widthProperty());
        textField.prefWidthProperty().bind(column.widthProperty());
        textField.textProperty().addListener((ob, o, n) -> filterTable());
        input.getChildren().add(textField);
    }

    private void createColumn(TableView<Typ> table, HBox input, String label, String name, double width) {
        TableColumn<Typ, Object> column = new TableColumn<>(label);
        column.setMaxWidth(400);
        if (width != 0) {
            column.setPrefWidth(width);
        }
        column.setCellValueFactory(new PropertyValueFactory<>(name));
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
            getter = new PropertyDescriptor(name, typClass).getReadMethod();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        TextField textField = new TextField();
        textField.minWidthProperty().bind(column.widthProperty());
        textField.prefWidthProperty().bind(column.widthProperty());
        textField.textProperty().addListener((ob, o, n) -> filterTable());
        input.getChildren().add(textField);

        FilterContainer container = new FilterContainer();
        container.textField = textField;
        container.method = getter;
        filterContainers.add(container);
    }

    private static class FilterContainer {
        private TextField textField;
        private Method method;
    }
}
