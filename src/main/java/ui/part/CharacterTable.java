package ui.part;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import manager.LanguageUtility;

import java.util.function.Function;

public class CharacterTable<Obj> extends VBox {

    protected ObservableList<Obj> equipment;
    protected ObservableList<CharacterColumn<Obj>> columns;

    public CharacterTable(ObservableList<Obj> equipment) {
        this.equipment = equipment;
        this.columns = FXCollections.observableArrayList();

        this.columns.addListener((ListChangeListener<? super CharacterColumn<Obj>>) change -> reload());
        this.equipment.addListener((ListChangeListener<? super Obj>) change -> reload());
    }

    protected void reload() {
        this.getChildren().clear();

        HBox headline = new HBox();
        this.getChildren().add(headline);

        for (CharacterColumn<Obj> column : columns) {
            headline.getChildren().add(column.header);
        }

        for (Obj obj : equipment) {
            HBox line = new HBox();
            this.getChildren().add(line);

            for (CharacterColumn<Obj> column : columns) {
                Region region = column.cellFactory.apply(obj);
                region.minHeightProperty().bind(line.heightProperty());
                line.getChildren().add(region);
            }
        }
    }

    public void addStringColumn(String key, int width, Function<Obj, Object> getter) {
        final boolean firstCol = columns.isEmpty();

        columns.add(new CharacterColumn<>(
                createEquipmentHeadline(key, width, !firstCol),
                obj -> createEquipmentLabel(new ReadOnlyStringWrapper(String.valueOf(getter.apply(obj))), width, !firstCol)
        ));
    }

    public void addColumn(String key, int width, Function<Obj, ObservableValue<?>> getter) {
        final boolean firstCol = columns.isEmpty();

        columns.add(new CharacterColumn<>(
                createEquipmentHeadline(key, width, !firstCol),
                obj -> createEquipmentLabel(getter.apply(obj), width, !firstCol)
        ));
    }

    protected Region createEquipmentHeadline(String key, double width, boolean left) {
        Label label = new Label();
        label.textProperty().bind(LanguageUtility.getMessageProperty(key));
        label.setPrefWidth(width);
        label.setTextAlignment(TextAlignment.LEFT);
        label.setWrapText(true);
        label.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID,
                left ? BorderStrokeStyle.SOLID : BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        label.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        label.setPadding(new Insets(0, 0, 0, 5));

        return label;
    }

    protected Region createEquipmentLabel(ObservableValue<?> val, double width, boolean left) {
        return createEquipmentLabel(val, width, Color.TRANSPARENT, left, true);
    }

    protected Region createEquipmentLabel(ObservableValue<?> val, double width, Color color, boolean left, boolean bottom) {
        Label label = new Label(String.valueOf(val.getValue()));
        val.addListener((ob, o, n) -> label.setText(String.valueOf(val.getValue())));
        label.setPrefWidth(width);
        label.setTextAlignment(TextAlignment.LEFT);
        label.setWrapText(true);
        label.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                bottom ? BorderStrokeStyle.SOLID : BorderStrokeStyle.NONE,
                left ? BorderStrokeStyle.SOLID : BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        label.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        label.setPadding(new Insets(0, 0, 0, 5));

        return label;
    }

    protected static class CharacterColumn<Eq> {
        public Node header;
        public Function<Eq, Region> cellFactory;

        public CharacterColumn(Node header, Function<Eq, Region> cellFactory) {
            this.header = header;
            this.cellFactory = cellFactory;
        }
    }
}
