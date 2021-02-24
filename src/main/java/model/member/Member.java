package model.member;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.member.interfaces.IMember;

public abstract class Member implements IMember {

    protected StringProperty name;

    protected Member() {
        this.name = new SimpleStringProperty("");
    }

    protected void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }
}
