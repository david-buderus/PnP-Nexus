package de.pnp.manager.ui.part;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilteredTableView<S> extends VBox {

    protected ArrayList<FilterContainer<S>> filterContainers;
    protected TableView<S> tableView;
    protected ListProperty<S> list;
    protected ListProperty<S> sourceList;
    protected HBox inputFields;

    public FilteredTableView(ListProperty<S> sourceList) {
        this.filterContainers = new ArrayList<>();
        this.sourceList = sourceList;

        this.list = new SimpleListProperty<>();
        sourceList.addListener((ob, o, n) -> list.set(n.stream().collect(Collectors.toCollection(FXCollections::observableArrayList))));
        list.set(sourceList.stream().collect(Collectors.toCollection(FXCollections::observableArrayList)));

        this.tableView = new TableView<>();
        this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.tableView.itemsProperty().bindBidirectional(list);
        this.tableView.itemsProperty().addListener((ob, o, n) -> tableView.layout());
        this.tableView.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(tableView);

        this.inputFields = new HBox();
        this.getChildren().add(inputFields);
    }

    public void addColumn(String columnKey, Function<S, Object> getter) {
        addColumn(columnKey, getter, 0);
    }

    public void addColumn(String columnKey, Function<S, Object> getter, int width) {
        addObservableColumn(columnKey, getter.andThen(obj -> new ReadOnlyStringWrapper(String.valueOf(obj))), width);
    }

    public <Ob> void addObservableColumn(String columnKey, Function<S, ObservableValue<Ob>> getter) {
        addObservableColumn(columnKey, getter, 0);
    }

    public <Ob> void addObservableColumn(String columnKey, Function<S, ObservableValue<Ob>> getter, int width) {
        TableColumn<S, Ob> column = new WrappingTableColumn<>();
        column.textProperty().bind(LanguageUtility.getMessageProperty(columnKey));
        column.setCellValueFactory(cell -> getter.apply(cell.getValue()));
        tableView.getColumns().add(column);

        if (width > 0) {
            column.setPrefWidth(width);
        }

        TextField textField = new TextField();
        textField.minWidthProperty().bind(column.widthProperty());
        textField.prefWidthProperty().bind(column.widthProperty());
        textField.textProperty().addListener((ob, o, n) -> update());
        inputFields.getChildren().add(textField);

        filterContainers.add(new FilterContainer<>(textField, getter.andThen(ob -> String.valueOf(ob.getValue()))));
    }

    @SuppressWarnings("rawtypes")
    public void setColumnResizePolicy(Callback<TableView.ResizeFeatures, Boolean> callback) {
        tableView.setColumnResizePolicy(callback);
    }

    protected void update() {
        Stream<S> stream = sourceList.stream();

        for (FilterContainer<S> container : filterContainers) {
            stream = stream.filter(x -> String.valueOf(container.getter.apply(x)).toLowerCase().contains(container.textField.getText().toLowerCase()));
        }

        list.set(stream.collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    protected static class FilterContainer<S> {
        public final TextField textField;
        public final Function<S, Object> getter;

        public FilterContainer(TextField textField, Function<S, Object> getter) {
            this.textField = textField;
            this.getter = getter;
        }
    }
}
