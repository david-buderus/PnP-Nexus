package ui.part;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListCell;
import model.interfaces.WithToStringProperty;

public class UpdatingListCell<T extends WithToStringProperty> extends ListCell<T> {

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.toStringProperty().get());
            item.toStringProperty().addListener(createUpdate(item));
        }
    }

    private ChangeListener<? super String> createUpdate(T item) {
        return new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends String> ob, String o, String n) {
                if (getItem() != item) {
                    ob.removeListener(this);
                } else {
                    setText(n);
                }
            }
        };
    }
}
