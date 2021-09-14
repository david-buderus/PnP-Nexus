package de.pnp.manager.model.member.part;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SecondaryAttributeModifier {

    protected IntegerProperty modifier;
    protected IntegerBinding result;

    public SecondaryAttributeModifier(int base) {
        this(base, 0);
    }

    public SecondaryAttributeModifier(int base, int startValue) {
        this.modifier = new SimpleIntegerProperty(startValue);
        this.result = Bindings.createIntegerBinding(() -> base + modifier.get(), modifier);
    }

    public int getModifier() {
        return modifier.get();
    }

    public IntegerProperty modifierProperty() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier.set(modifier);
    }

    public int getResult() {
        return result.get();
    }

    public IntegerBinding resultProperty() {
        return result;
    }
}
