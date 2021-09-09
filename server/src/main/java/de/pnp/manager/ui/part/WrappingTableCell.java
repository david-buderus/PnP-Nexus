package de.pnp.manager.ui.part;

import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class WrappingTableCell<S, T> extends TableCell<S, T> {

    protected final int xPadding;
    protected final int yPadding;

    public WrappingTableCell() {
        this(10, 0);
    }

    public WrappingTableCell(int xPadding, int yPadding) {
        this.xPadding = xPadding;
        this.yPadding = yPadding;
    }

    @Override
    public void updateItem(T t, boolean empty) {
        super.updateItem(t, empty);
        String item = empty ? "" : String.valueOf(t);

        Text text = new Text(item);
        text.setStyle(" -fx-text-wrap: true;");

        synchronizeTextSizeWithCellSize(text);
        this.widthProperty().addListener((ob, o, n) -> synchronizeTextSizeWithCellSize(text));

        this.setGraphic(text);
    }

    private void synchronizeTextSizeWithCellSize(Text text) {
        double width = this.getTableColumn().getWidth() - xPadding;
        text.setWrappingWidth(width);
        this.setPrefHeight(text.getLayoutBounds().getHeight() + yPadding);
    }
}
