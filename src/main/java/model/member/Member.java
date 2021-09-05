package model.member;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.member.interfaces.IMember;

import java.util.Map;

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

    protected Member(Map<String, String> parameterMap) {
        this.name = new SimpleStringProperty(parameterMap.getOrDefault("character.name", ""));
    }
}
