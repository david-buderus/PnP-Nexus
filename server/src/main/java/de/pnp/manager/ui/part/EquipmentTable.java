package de.pnp.manager.ui.part;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import de.pnp.manager.model.item.Equipment;

import java.util.function.Function;

public class EquipmentTable<Eq extends Equipment> extends CharacterTable<Eq> {

    protected BooleanProperty withSeparatedUpgradeLine;
    protected ObjectProperty<Function<Eq, ObservableValue<String>>> upgradeFactory;

    public EquipmentTable(ObservableList<Eq> equipment) {
        super(equipment);
        this.withSeparatedUpgradeLine = new SimpleBooleanProperty(true);
        this.upgradeFactory = new SimpleObjectProperty<>(eq -> new ReadOnlyStringWrapper(eq.upgradesAsString()));

        this.withSeparatedUpgradeLine.addListener((ob, o, n) -> reload());
        this.upgradeFactory.addListener((ob, o, n) -> reload());
    }

    @Override
    protected void reload() {
        this.getChildren().clear();

        HBox headline = new HBox();
        this.getChildren().add(headline);

        for (CharacterColumn<Eq> column : columns) {
            headline.getChildren().add(column.header);
        }

        for (Eq eq : equipment) {
            HBox line = new HBox();
            this.getChildren().add(line);

            int prefWidth = 0;

            for (CharacterColumn<Eq> column : columns) {
                Region region = column.cellFactory.apply(eq);
                prefWidth += region.getPrefWidth();
                region.minHeightProperty().bind(line.heightProperty());
                line.getChildren().add(region);
            }

            if (withSeparatedUpgradeLine.get()) {
                this.getChildren().add(createEquipmentLabel(upgradeFactory.get().apply(eq), prefWidth, Color.LIGHTGRAY, false, true));
            }
        }
    }

    @Override
    protected Region createEquipmentLabel(ObservableValue<?> val, double width, boolean left) {
        return createEquipmentLabel(val, width, Color.TRANSPARENT, left, !withSeparatedUpgradeLine.get());
    }

    public boolean isWithSeparatedUpgradeLine() {
        return withSeparatedUpgradeLine.get();
    }

    public BooleanProperty withSeparatedUpgradeLineProperty() {
        return withSeparatedUpgradeLine;
    }

    public void setWithSeparatedUpgradeLine(boolean withSeparatedUpgradeLine) {
        this.withSeparatedUpgradeLine.set(withSeparatedUpgradeLine);
    }

    public Function<Eq, ObservableValue<String>> getUpgradeFactory() {
        return upgradeFactory.get();
    }

    public ObjectProperty<Function<Eq, ObservableValue<String>>> upgradeFactoryProperty() {
        return upgradeFactory;
    }

    public void setUpgradeFactory(Function<Eq, ObservableValue<String>> upgradeFactory) {
        this.upgradeFactory.set(upgradeFactory);
    }
}
