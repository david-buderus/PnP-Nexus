package ui.part;

import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class WrappingTableCell<S, T> extends TableCell<S, T> {

    @Override
    public void updateItem(T t, boolean empty) {
        super.updateItem(t, empty);
        String item = empty ? "" : String.valueOf(t);

        Text text = new Text(item);
        text.setStyle(" -fx-text-wrap: true;");

        calculateSize(text);
        this.widthProperty().addListener((ob, o, n) -> calculateSize(text));

        this.setGraphic(text);
    }

    private void calculateSize(Text text) {
        double width = this.getTableColumn().getWidth() * 0.9;
        text.setWrappingWidth(width);
        this.setPrefHeight(text.getLayoutBounds().getHeight() + 10);
    }
}
