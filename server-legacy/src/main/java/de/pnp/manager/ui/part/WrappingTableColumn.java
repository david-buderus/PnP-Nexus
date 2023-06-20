package de.pnp.manager.ui.part;

import de.pnp.manager.main.LanguageUtility;
import javafx.scene.control.TableColumn;

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
