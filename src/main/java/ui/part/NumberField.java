package ui.part;

import javafx.beans.property.*;
import javafx.scene.control.TextField;
import org.apache.commons.lang.math.NumberUtils;

public class NumberField extends TextField {

    protected DoubleProperty number;
    protected BooleanProperty validInput;

    public NumberField() {
        this(0);
    }

    public NumberField(double number) {
        super(String.valueOf(number));
        this.number = new SimpleDoubleProperty(number);
        this.validInput = new SimpleBooleanProperty(true);
        this.textProperty().bindBidirectional(this.number, new NumStringConverter());
        this.textProperty().addListener((ob, o, n) -> {
            if(NumberUtils.isNumber(n)) {
                this.setStyle("");
                this.validInput.set(true);
            } else {
                this.setStyle("-fx-text-box-border: #B22222;");
                this.validInput.set(false);
            }
        });
    }

    public double getNumber() {
        return number.get();
    }

    public DoubleProperty numberProperty() {
        return number;
    }

    public void setNumber(double number) {
        this.number.set(number);
    }

    public boolean isValidInput() {
        return validInput.get();
    }

    public ReadOnlyBooleanProperty validInputProperty() {
        return validInput;
    }
}
