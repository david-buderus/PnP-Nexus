package model.member.interfaces;

import javafx.beans.property.StringProperty;

public interface IMember {
    String getName();
    public StringProperty nameProperty();
}
