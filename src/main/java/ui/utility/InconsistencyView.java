package ui.utility;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import manager.Utility;
import model.Inconsistency;
import ui.IView;
import ui.ViewPart;

public class InconsistencyView extends ViewPart {

    public InconsistencyView(IView parent) {
        super("Inkonsistenzen", parent);
        this.disableProperty().bind(Utility.inconsistent.not());

        VBox root = new VBox();
        root.setPadding(new Insets(20));

        TableView<Inconsistency> table = new TableView<>();
        table.itemsProperty().bind(Utility.inconsistencyList);
        root.getChildren().add(table);

        TableColumn<Inconsistency, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setPrefWidth(150);
        table.getColumns().add(name);

        TableColumn<Inconsistency, String> inconsistency = new TableColumn<>("Inkonsistenz");
        inconsistency.setCellValueFactory(new PropertyValueFactory<>("inconsistency"));
        inconsistency.setPrefWidth(150);
        table.getColumns().add(inconsistency);

        TableColumn<Inconsistency, String> info = new TableColumn<>("Weitere Infos");
        info.setCellValueFactory(x -> new ReadOnlyStringWrapper(x.getValue().getInfoAsString()));
        info.setPrefWidth(600);
        table.getColumns().add(info);

        this.setContent(root);
    }
}
