package de.pnp.manager.ui.part;

import javafx.util.converter.NumberStringConverter;

import java.text.MessageFormat;

public class NumStringConverter extends NumberStringConverter {

    int defaultValue;

    /**
     * NumStringConverter with the default value of 0
     */
    public NumStringConverter() {
        this(0);
    }

    public NumStringConverter(int defaultValue) {
        super();
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString(Number value) {
        if (value == null) {
            return "";
        } else {
            return MessageFormat.format("{0,number,#}", value);
        }
    }

    @Override
    public Number fromString(String string) {
        try {
            Number n = super.fromString(string);

            if (Math.rint(n.doubleValue()) == n.doubleValue()) {
                return n.intValue();
            } else {
                return n;
            }

        } catch (Exception e) {
            return defaultValue;
        }
    }
}
