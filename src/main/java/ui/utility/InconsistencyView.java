package ui.utility;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import manager.Database;
import model.Inconsistency;
import ui.IView;
import ui.ViewPart;

import static manager.LanguageUtility.getMessageProperty;

public class InconsistencyView extends ViewPart {

    public InconsistencyView(IView parent) {
        super("inconsistencies.title", parent);
        this.disableProperty().bind(Database.inconsistent.not());

        VBox root = new VBox();
        root.setPadding(new Insets(20));

        TableView<Inconsistency> table = new TableView<>();
        table.itemsProperty().bind(Database.inconsistencyList);
        root.getChildren().add(table);

        TableColumn<Inconsistency, String> name = new TableColumn<>();
        name.textProperty().bind(getMessageProperty("column.name"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setPrefWidth(150);
        table.getColumns().add(name);

        TableColumn<Inconsistency, String> inconsistency = new TableColumn<>();
        name.textProperty().bind(getMessageProperty("inconsistencies.column.inconsistencies"));
        inconsistency.setCellValueFactory(new PropertyValueFactory<>("inconsistency"));
        inconsistency.setPrefWidth(150);
        table.getColumns().add(inconsistency);

        TableColumn<Inconsistency, String> info = new TableColumn<>();
        name.textProperty().bind(getMessageProperty("inconsistencies.column.moreInfo"));
        info.setCellValueFactory(x -> new ReadOnlyStringWrapper(x.getValue().getInfoAsString()));
        info.setPrefWidth(600);
        table.getColumns().add(info);

        this.setContent(root);
    }
}
