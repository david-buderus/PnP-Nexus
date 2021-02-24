package model.member;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Member {

    protected StringProperty name;

    protected Member() {
        this.name = new SimpleStringProperty("");
    }

    public String getName() {
        return this.name.get();
    }

    protected void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }
}
