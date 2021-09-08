package ui.part;

import javafx.scene.control.TableColumn;
import manager.LanguageUtility;

public class WrappingTableColumn<S, T> extends TableColumn<S, T> {

    public WrappingTableColumn() {
        super();
        this.setCellFactory(col -> new WrappingTableCell<>());
    }

    public WrappingTableColumn(String key) {
        this();
        this.textProperty().bind(LanguageUtility.getMessageProperty(key));
    }
}
