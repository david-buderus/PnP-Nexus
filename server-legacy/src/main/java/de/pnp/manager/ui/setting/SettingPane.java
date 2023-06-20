package de.pnp.manager.ui.setting;

import de.pnp.manager.main.Utility;
import de.pnp.manager.ui.part.NumberField;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.apache.commons.configuration2.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

import static de.pnp.manager.main.LanguageUtility.getMessageProperty;

public abstract class SettingPane<Content> {

    public static SettingPane<?> convertSettingLine(String input) {
        final String[] values = Arrays.stream(input.split(",")).map(String::trim).toArray(String[]::new);
        SettingLine line = new SettingLine(values[0], values[1], SettingType.valueOf(values[2]));

        switch (line.getType()) {
            case INTEGER:
                return new SettingPane.IntegerPane(line);
            case DOUBLE:
                return new SettingPane.DoublePane(line);
            case STRING:
                return new SettingPane.StringPane(line);
            case BOOLEAN:
                return new SettingPane.BooleanPane(line);
            case PERCENT:
                return new SettingPane.PercentPane(line);
            case STRING_LIST:
                return new SettingPane.StringListPane(line);
            case INTEGER_LIST:
                return new SettingPane.IntListPane(line);
            case DOUBLE_LIST:
                return new SettingPane.DoubleListPane(line);
        }
        throw new RuntimeException("Unknown SettingType");
    }

    protected Label label;
    protected Region control;
    protected Property<Content> property;
    protected BooleanProperty dirty;
    protected Button resetButton;
    protected Button defaultButton;
    protected Configuration config;
    protected Configuration defaultConfig;
    protected SettingLine settingLine;

    public SettingPane(SettingLine line, SettingType expectedType) {

        if (line.getType() != expectedType) {
            throw new RuntimeException("Wrong Type");
        }

        this.settingLine = line;
        this.dirty = new SimpleBooleanProperty(false);
        this.config = Utility.getConfig();
        this.defaultConfig = Utility.getDefaultConfig();

        label = new Label();
        label.setPrefWidth(200);
        label.setWrapText(true);
        label.textProperty().bind(getMessageProperty(line.getUnlocalizedName())
                .concat(Bindings.createStringBinding(() -> dirtyProperty().get() ? "*" : "", dirtyProperty())));

        resetButton = new Button();
        resetButton.setPrefWidth(100);
        resetButton.textProperty().bind(getMessageProperty("settings.button.reset"));
        resetButton.disableProperty().bind(dirtyProperty().not());
        resetButton.setOnAction(ev -> reset());

        defaultButton = new Button();
        defaultButton.setPrefWidth(100);
        defaultButton.textProperty().bind(getMessageProperty("settings.button.default"));
        defaultButton.setOnAction(ev -> toDefault());
    }

    protected void init() {
        control.setPrefWidth(250);
        defaultButton.setDisable(compare(getContent(), getDefaultKeyValue()));
        property.addListener((ob, o, n) -> dirty.set(!compare(n, getKeyValue())));
        property.addListener((ob, o, n) -> defaultButton.setDisable(compare(n, getDefaultKeyValue())));
    }

    protected boolean compare(Content a, Content b) {
        return a.equals(b);
    }

    public void addTo(GridPane pane, int row) {
        pane.add(label, 0, row);
        pane.add(control, 1, row);
        pane.add(resetButton, 2, row);
        pane.add(defaultButton, 3, row);
    }

    public boolean isDirty() {
        return dirty.get();
    }

    public ReadOnlyBooleanProperty dirtyProperty() {
        return dirty;
    }

    public void checkDirty() {
        dirty.set(!compare(getContent(), getKeyValue()));
    }

    public Content getContent() {
        return property.getValue();
    }

    public String getKey() {
        return settingLine.getKey();
    }

    public void reset() {
        property.setValue(getKeyValue());
    }

    public void toDefault() {
        property.setValue(getDefaultKeyValue());
    }

    protected abstract Content getKeyValue();

    protected abstract Content getDefaultKeyValue();

    protected static class StringPane extends SettingPane<String> {

        public StringPane(SettingLine line) {
            super(line, SettingType.STRING);

            TextField field = new TextField(getKeyValue());
            control = field;
            property = field.textProperty();

            init();
        }

        @Override
        protected String getKeyValue() {
            return config.getString(settingLine.getKey(), "");
        }

        @Override
        protected String getDefaultKeyValue() {
            return defaultConfig.getString(settingLine.getKey(), "");
        }
    }

    protected static class BooleanPane extends SettingPane<Boolean> {

        public BooleanPane(SettingLine line) {
            super(line, SettingType.BOOLEAN);

            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(getKeyValue());
            control = checkBox;
            property = checkBox.selectedProperty();

            init();
        }

        @Override
        protected Boolean getKeyValue() {
            return config.getBoolean(settingLine.getKey(), false);
        }

        @Override
        protected Boolean getDefaultKeyValue() {
            return defaultConfig.getBoolean(settingLine.getKey(), false);
        }
    }

    protected abstract static class NumberPane extends SettingPane<Number> {

        public NumberPane(SettingLine line, SettingType expectedType) {
            super(line, expectedType);
        }

        @Override
        protected boolean compare(Number a, Number b) {
            return a.doubleValue() == b.doubleValue();
        }
    }

    protected static class IntegerPane extends NumberPane {

        public IntegerPane(SettingLine line) {
            super(line, SettingType.INTEGER);

            NumberField field = new NumberField(getKeyValue());
            control = field;
            property = field.numberProperty();

            init();
        }

        @Override
        public Integer getContent() {
            return property.getValue().intValue();
        }

        @Override
        protected Integer getKeyValue() {
            return config.getInt(settingLine.getKey(), 0);
        }

        @Override
        protected Integer getDefaultKeyValue() {
            return defaultConfig.getInt(settingLine.getKey(), 0);
        }
    }

    protected static class DoublePane extends NumberPane {

        public DoublePane(SettingLine line) {
            super(line, SettingType.INTEGER);

            NumberField field = new NumberField(getKeyValue());
            control = field;
            property = field.numberProperty();

            init();
        }

        @Override
        public Double getContent() {
            return property.getValue().doubleValue();
        }

        @Override
        protected Double getKeyValue() {
            return config.getDouble(settingLine.getKey(), 0);
        }

        @Override
        protected Double getDefaultKeyValue() {
            return defaultConfig.getDouble(settingLine.getKey(), 0);
        }
    }

    protected static class PercentPane extends NumberPane {


        public PercentPane(SettingLine line) {
            super(line, SettingType.PERCENT);

            HBox box = new HBox(2);
            box.setAlignment(Pos.CENTER);
            control = box;

            Slider slider = new Slider(0, 1, getKeyValue());
            slider.setPrefWidth(240);
            slider.valueProperty().addListener((ob, o, n) -> {
                final double roundedValue = Math.floor(n.doubleValue() * 100) / 100;
                slider.valueProperty().set(roundedValue);
            });
            box.getChildren().add(slider);
            property = slider.valueProperty();

            NumberField field = new NumberField();
            field.numberProperty().bindBidirectional(slider.valueProperty());
            box.getChildren().add(field);

            init();
        }

        @Override
        public Double getContent() {
            return property.getValue().doubleValue();
        }

        @Override
        protected Double getKeyValue() {
            return config.getDouble(settingLine.getKey(), 0);
        }

        @Override
        protected Double getDefaultKeyValue() {
            return defaultConfig.getDouble(settingLine.getKey(), 0);
        }
    }

    protected static abstract class ListPane<Content> extends SettingPane<Content[]> {

        public ListPane(SettingLine line, SettingType expectedType) {
            super(line, expectedType);
        }

        @Override
        protected boolean compare(Content[] a, Content[] b) {
            return Arrays.equals(a, b);
        }
    }

    protected static class StringListPane extends ListPane<String> {

        protected ObjectProperty<String[]> arrayProperty;

        public StringListPane(SettingLine line) {
            super(line, SettingType.STRING_LIST);

            TextField field = new TextField(String.join(", ", getKeyValue()));
            control = field;

            arrayProperty = new SimpleObjectProperty<>(convert(field.getText()));
            field.textProperty().addListener((ob, o, n) -> arrayProperty.set(convert(n)));
            arrayProperty.addListener((ob, o, n) -> field.textProperty().set(String.join(", ", n)));
            property = arrayProperty;

            init();
        }

        protected String[] convert(String input) {
            return Arrays.stream(input.split(",")).map(String::trim).toArray(String[]::new);
        }

        @Override
        protected String[] getKeyValue() {
            return config.getStringArray(settingLine.getKey());
        }

        @Override
        protected String[] getDefaultKeyValue() {
            return defaultConfig.getStringArray(settingLine.getKey());
        }
    }

    protected static class IntListPane extends ListPane<Integer> {

        protected ObjectProperty<Integer[]> arrayProperty;

        public IntListPane(SettingLine line) {
            super(line, SettingType.INTEGER_LIST);

            TextField field = new TextField(Arrays.stream(getKeyValue()).map(String::valueOf).collect(Collectors.joining(", ")));
            control = field;

            arrayProperty = new SimpleObjectProperty<>(convert(field.getText()));
            field.textProperty().addListener((ob, o, n) -> arrayProperty.set(convert(n)));
            arrayProperty.addListener((ob, o, n) -> field.textProperty().set(Arrays.stream(n).map(String::valueOf).collect(Collectors.joining(", "))));
            property = arrayProperty;

            init();
        }

        protected Integer[] convert(String input) {
            return Arrays.stream(input.split(",")).map(s -> Integer.parseInt(s.trim())).toArray(Integer[]::new);
        }

        @Override
        protected Integer[] getKeyValue() {
            return (Integer[]) config.getArray(Integer.class, settingLine.getKey());
        }

        @Override
        protected Integer[] getDefaultKeyValue() {
            return (Integer[]) defaultConfig.getArray(Integer.class, settingLine.getKey());
        }
    }

    protected static class DoubleListPane extends ListPane<Double> {

        protected ObjectProperty<Double[]> arrayProperty;

        public DoubleListPane(SettingLine line) {
            super(line, SettingType.DOUBLE_LIST);

            TextField field = new TextField(Arrays.stream(getKeyValue()).map(String::valueOf).collect(Collectors.joining(", ")));
            control = field;

            arrayProperty = new SimpleObjectProperty<>(convert(field.getText()));
            field.textProperty().addListener((ob, o, n) -> arrayProperty.set(convert(n)));
            arrayProperty.addListener((ob, o, n) -> field.textProperty().set(Arrays.stream(n).map(String::valueOf).collect(Collectors.joining(", "))));
            property = arrayProperty;

            init();
        }

        protected Double[] convert(String input) {
            return Arrays.stream(input.split(",")).map(Double::valueOf).toArray(Double[]::new);
        }

        @Override
        protected Double[] getKeyValue() {
            return (Double[]) config.getArray(Double.class, settingLine.getKey());
        }

        @Override
        protected Double[] getDefaultKeyValue() {
            return (Double[]) defaultConfig.getArray(Double.class, settingLine.getKey());
        }
    }
}
